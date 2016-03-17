package com.firstlinesoftware.delivery.misc.rates;

import com.firstlinesoftware.delivery.misc.rates.dto.ExcelCityDto;
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

    private Map<CellCoordinate, String> cityColumns = initCityColumns();
    private ItemContainerFactory itemContainerFactory = new ItemContainerFactory();
    private SimpleProcessMessagesHolder messagesHolder = new SimpleProcessMessagesHolder();

    private final Consumer<ExcelCityDto> dtoConsumer;

    private Map<CellCoordinate, String> initCityColumns() {
        HashMap<CellCoordinate, String> columns = new HashMap<>();
        columns.put(new CellCoordinate(0, 0), ExcelCityDto.Fileds.code.name());
        columns.put(new CellCoordinate(1, 0), ExcelCityDto.Fileds.name.name());
        return columns;
    }

    public static void main(String[] args) {
        Storage storage = new Storage();
        ExcelCityProcessor dtoProcessor = new ExcelCityProcessor(storage);

//        storage.remove();
        storage.open();

        dtoProcessor.init(); //always after opening storage

        storage.doInTransaction(() -> {
            TransportRatesParser parser = new TransportRatesParser(dtoProcessor::process);
            parser.parse();
            System.out.format("Items processed");

            SimpleProcessMessagesHolder messagesHolder1 = parser.getMessagesHolder();
            for (int i = 0; i < messagesHolder1.count(); i++) {
                System.out.println(messagesHolder1.get(i));
            }

        });
        storage.close();
    }

    public TransportRatesParser(Consumer<ExcelCityDto> dtoConsumer) {
        this.dtoConsumer = dtoConsumer;
    }

    private void parse() {
        File miscFolder = new File(TransportRatesParser.miscFolder);
        File ratesArchive = new File(miscFolder, dictFileName);

        if (ratesArchive.exists()) {
            try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(ratesArchive))) {
                CellGroup group = initCellGroup();
                Workbook wb = new XSSFWorkbook(in);

                processSheets(wb, group);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.format("Dictionary file %s can not be found in folder %s. Import operation is canceled", ratesArchive.getName(), miscFolder.getAbsolutePath());
        }
    }

    private void processSheets(Workbook wb, CellGroup group) {
        Sheet sheet = wb.getSheetAt(0);

        ItemContainer container = itemContainerFactory.createItemContainer(sheet, new CellCoordinate(0, 0));
        ExcelCityDto dto = null;
        while (!isEmpty(dto)) {
            dto = container.readItem(ExcelCityDto.class, group, messagesHolder);
            if (!isEmpty(dto)) {
                dtoConsumer.accept(dto);
            }
        }
    }

    private boolean isEmpty(ExcelCityDto dto) {
        return dto != null && StringUtils.isBlank(dto.getCode()) && StringUtils.isBlank(dto.getName());
    }

    @NotNull
    private CellGroup initCellGroup() {
        //Define row cell group
        CellGroup group = new CellGroup();
        for (Map.Entry<CellCoordinate, String> col : cityColumns.entrySet()) {
            group.addCell(col.getKey(), new BeanPropertyValueReference(col.getValue()));
        }
        return group;
    }

    public SimpleProcessMessagesHolder getMessagesHolder() {
        return messagesHolder;
    }
}
