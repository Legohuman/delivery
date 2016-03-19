package com.firstlinesoftware.delivery.misc.rates;

import com.firstlinesoftware.delivery.misc.rates.dto.ExcelCityDto;
import com.firstlinesoftware.delivery.misc.rates.dto.ExcelContainerTypeTransportRateDto;
import com.firstlinesoftware.delivery.misc.rates.dto.ExcelDimensionBasedTransportRateDto;
import com.firstlinesoftware.delivery.storage.map.Storage;
import com.github.excelmapper.core.engine.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * User: Legohuman
 * Date: 05/03/16
 */
public class TransportRatesParser {

    public static final String miscFolder = "misc";
    public static final String dictFileName = "transport-rates.xlsx";

    private ItemContainerFactory itemContainerFactory = new ItemContainerFactory();
    private SimpleProcessMessagesHolder messagesHolder = new SimpleProcessMessagesHolder();

    private final Consumer<ExcelCityDto> cityConsumer;
    private final Consumer<ExcelDimensionBasedTransportRateDto> emskAviaConsumer;
    private final Consumer<ExcelDimensionBasedTransportRateDto> emskRzdConsumer;
    private final Consumer<ExcelContainerTypeTransportRateDto> fescoShipConsumer;

    private Map<CellCoordinate, String> initCityColumns() {
        HashMap<CellCoordinate, String> columns = new HashMap<>();
        columns.put(new CellCoordinate(0, 0), ExcelCityDto.Fileds.code.name());
        columns.put(new CellCoordinate(1, 0), ExcelCityDto.Fileds.name.name());
        columns.put(new CellCoordinate(2, 0), ExcelCityDto.Fileds.countryCode.name());
        return columns;
    }

    private Map<CellCoordinate, String> initEmskAviaColumns() {
        HashMap<CellCoordinate, String> columns = new HashMap<>();
        columns.put(new CellCoordinate(0, 0), ExcelDimensionBasedTransportRateDto.Fileds.fromCode.name());
        columns.put(new CellCoordinate(2, 0), ExcelDimensionBasedTransportRateDto.Fileds.toCode.name());
        columns.put(new CellCoordinate(4, 0), ExcelDimensionBasedTransportRateDto.Fileds.minCost.name());
        columns.put(new CellCoordinate(5, 0), ExcelDimensionBasedTransportRateDto.Fileds.weightRate.name());
        columns.put(new CellCoordinate(6, 0), ExcelDimensionBasedTransportRateDto.Fileds.duration.name());
        return columns;
    }

    private Map<CellCoordinate, String> initEmskRailColumns() {
        HashMap<CellCoordinate, String> columns = new HashMap<>();
        columns.put(new CellCoordinate(0, 0), ExcelDimensionBasedTransportRateDto.Fileds.fromCode.name());
        columns.put(new CellCoordinate(2, 0), ExcelDimensionBasedTransportRateDto.Fileds.toCode.name());
        columns.put(new CellCoordinate(4, 0), ExcelDimensionBasedTransportRateDto.Fileds.minCost.name());
        columns.put(new CellCoordinate(5, 0), ExcelDimensionBasedTransportRateDto.Fileds.weightRate.name());
        columns.put(new CellCoordinate(6, 0), ExcelDimensionBasedTransportRateDto.Fileds.volumeRate.name());
        columns.put(new CellCoordinate(7, 0), ExcelDimensionBasedTransportRateDto.Fileds.duration.name());
        return columns;
    }

    private Map<CellCoordinate, String> initFescoShipColumns() {
        HashMap<CellCoordinate, String> columns = new HashMap<>();
        columns.put(new CellCoordinate(0, 0), ExcelContainerTypeTransportRateDto.Fileds.fromCode.name());
        columns.put(new CellCoordinate(2, 0), ExcelContainerTypeTransportRateDto.Fileds.toCode.name());
        columns.put(new CellCoordinate(4, 0), ExcelContainerTypeTransportRateDto.Fileds.containerType.name());
        columns.put(new CellCoordinate(5, 0), ExcelContainerTypeTransportRateDto.Fileds.containerRate.name());
        columns.put(new CellCoordinate(6, 0), ExcelContainerTypeTransportRateDto.Fileds.duration.name());
        return columns;
    }

    public static void main(String[] args) {
        Storage storage = new Storage();
        ExcelCityProcessor cityProcessor = new ExcelCityProcessor(storage);
        ExcelEmskAviaProcessor emskAviaProcessor = new ExcelEmskAviaProcessor(storage);
        ExcelEmskRzdProcessor emskRzdProcessor = new ExcelEmskRzdProcessor(storage);
        ExcelFescoShipProcessor fescoShipProcessor = new ExcelFescoShipProcessor(storage);

//        storage.remove();
        storage.open();

        cityProcessor.init(); //always after opening storage
        emskAviaProcessor.init(); //always after opening storage
        emskRzdProcessor.init(); //always after opening storage
        fescoShipProcessor.init(); //always after opening storage

        storage.doInTransaction(() -> {
            TransportRatesParser parser = new TransportRatesParser(cityProcessor::process,
                    emskAviaProcessor::process,
                    emskRzdProcessor::process,
                    fescoShipProcessor::process);
            parser.parse();
            System.out.format("Items processed");

            SimpleProcessMessagesHolder messagesHolder1 = parser.getMessagesHolder();
            for (int i = 0; i < messagesHolder1.count(); i++) {
                System.out.println(messagesHolder1.get(i));
            }

        });
        storage.close();
    }

    public TransportRatesParser(Consumer<ExcelCityDto> cityConsumer,
                                Consumer<ExcelDimensionBasedTransportRateDto> emskAviaConsumer,
                                Consumer<ExcelDimensionBasedTransportRateDto> emskRzdConsumer,
                                Consumer<ExcelContainerTypeTransportRateDto> fescoShipConsumer) {
        this.cityConsumer = cityConsumer;
        this.emskAviaConsumer = emskAviaConsumer;
        this.emskRzdConsumer = emskRzdConsumer;
        this.fescoShipConsumer = fescoShipConsumer;
    }

    private void parse() {
        File miscFolder = new File(TransportRatesParser.miscFolder);
        File ratesArchive = new File(miscFolder, dictFileName);

        if (ratesArchive.exists()) {
            try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(ratesArchive))) {
                Workbook wb = new XSSFWorkbook(in);

                processCitySheet(wb);
                processEmskAviaSheet(wb);
                processEmskRzdSheet(wb);
                processFescoShipSheet(wb);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.format("Dictionary file %s can not be found in folder %s. Import operation is canceled", ratesArchive.getName(), miscFolder.getAbsolutePath());
        }
    }

    private void processCitySheet(Workbook wb) {
        CellGroup group = initCellGroup(initCityColumns());
        Sheet sheet = wb.getSheetAt(0);

        ItemContainer container = itemContainerFactory.createItemContainer(sheet, new CellCoordinate(0, 0));
        ExcelCityDto dto = null;
        while (!isEmpty(dto)) {
            dto = container.readItem(ExcelCityDto.class, group, messagesHolder);
            if (!isEmpty(dto)) {
                cityConsumer.accept(dto);
            }
        }
    }

    private void processEmskAviaSheet(Workbook wb) {
        CellGroup group = initCellGroup(initEmskAviaColumns());
        Sheet sheet = wb.getSheetAt(1);

        ItemContainer container = itemContainerFactory.createItemContainer(sheet, new CellCoordinate(0, 1));

        ExcelDimensionBasedTransportRateDto dto = null;
        while (!isEmpty(dto)) {
            dto = container.readItem(ExcelDimensionBasedTransportRateDto.class, group, messagesHolder);
            if (!isEmpty(dto)) {
                emskAviaConsumer.accept(dto);
            }
        }
    }

    private void processEmskRzdSheet(Workbook wb) {
        CellGroup group = initCellGroup(initEmskRailColumns());
        Sheet sheet = wb.getSheetAt(2);

        ItemContainer container = itemContainerFactory.createItemContainer(sheet, new CellCoordinate(0, 1));

        ExcelDimensionBasedTransportRateDto dto = null;
        while (!isEmpty(dto)) {
            dto = container.readItem(ExcelDimensionBasedTransportRateDto.class, group, messagesHolder);
            if (!isEmpty(dto)) {
                emskRzdConsumer.accept(dto);
            }
        }
    }

    private void processFescoShipSheet(Workbook wb) {
        CellGroup group = initCellGroup(initFescoShipColumns());
        Sheet sheet = wb.getSheetAt(3);

        ItemContainer container = itemContainerFactory.createItemContainer(sheet, new CellCoordinate(0, 1));

        ExcelContainerTypeTransportRateDto dto = null;
        while (!isEmpty(dto)) {
            dto = container.readItem(ExcelContainerTypeTransportRateDto.class, group, messagesHolder);
            if (!isEmpty(dto)) {
                fescoShipConsumer.accept(dto);
            }
        }
    }

    private boolean isEmpty(ExcelCityDto dto) {
        return dto != null && StringUtils.isBlank(dto.getCode()) && StringUtils.isBlank(dto.getName());
    }

    private boolean isEmpty(ExcelDimensionBasedTransportRateDto dto) {
        return dto != null && StringUtils.isBlank(dto.getFromCode())
                && StringUtils.isBlank(dto.getToCode())
                && StringUtils.isBlank(dto.getMinCost())
                && StringUtils.isBlank(dto.getWeightRate())
                && StringUtils.isBlank(dto.getVolumeRate())
                && StringUtils.isBlank(dto.getDuration());
    }

    private boolean isEmpty(ExcelContainerTypeTransportRateDto dto) {
        return dto != null && StringUtils.isBlank(dto.getFromCode())
                && StringUtils.isBlank(dto.getToCode())
                && StringUtils.isBlank(dto.getContainerRate())
                && StringUtils.isBlank(dto.getContainerType())
                && StringUtils.isBlank(dto.getDuration());
    }

    @NotNull
    private CellGroup initCellGroup(Map<CellCoordinate, String> columns) {
        //Define row cell group
        CellGroup group = new CellGroup();
        for (Map.Entry<CellCoordinate, String> col : columns.entrySet()) {
            group.addCell(col.getKey(), new BeanPropertyValueReference(col.getValue()));
        }
        return group;
    }

    public SimpleProcessMessagesHolder getMessagesHolder() {
        return messagesHolder;
    }
}
