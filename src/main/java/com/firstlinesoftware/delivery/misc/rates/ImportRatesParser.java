package com.firstlinesoftware.delivery.misc.rates;

import com.firstlinesoftware.delivery.misc.rates.dto.ExcelImportRateDto;
import com.firstlinesoftware.delivery.storage.map.Storage;
import com.github.excelmapper.core.engine.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * User: Legohuman
 * Date: 05/03/16
 */
public class ImportRatesParser {

    public static final String miscFolder = "misc";
    public static final String dictFileName = "import-rates.xls";

    private Map<Integer, List<CellCoordinate>> tableStartCells = initTableStartCells();
    private Map<CellCoordinate, String> columns = initColumns();
    private ItemContainerFactory itemContainerFactory = new ItemContainerFactory();
    private SimpleProcessMessagesHolder messagesHolder = new SimpleProcessMessagesHolder();

    private final Consumer<ExcelImportRateDto> dtoConsumer;

    private Map<CellCoordinate, String> initColumns() {
        HashMap<CellCoordinate, String> columns = new HashMap<>();
        columns.put(new CellCoordinate(0, 0), ExcelImportRateDto.Fileds.code.name());
        columns.put(new CellCoordinate(1, 0), ExcelImportRateDto.Fileds.name.name());
        columns.put(new CellCoordinate(3, 0), ExcelImportRateDto.Fileds.desc.name());
        return columns;
    }

    public static void main(String[] args) {
        Storage storage = new Storage();
        ExcelImportRateProcessor dtoProcessor = new ExcelImportRateProcessor(storage);

        storage.remove();
        storage.open();

        dtoProcessor.init(); //always after opening storage

        storage.doInTransaction(() -> {
            ImportRatesParser parser = new ImportRatesParser(dtoProcessor::process);
            parser.parse();
            System.out.format("%s items processed", dtoProcessor.processedCount());

            SimpleProcessMessagesHolder messagesHolder1 = parser.getMessagesHolder();
            for (int i = 0; i < messagesHolder1.count(); i++) {
                System.out.println(messagesHolder1.get(i));
            }

        });
        storage.close();
    }

    public ImportRatesParser(Consumer<ExcelImportRateDto> dtoConsumer) {
        this.dtoConsumer = dtoConsumer;
    }

    private void parse() {
        File miscFolder = new File(ImportRatesParser.miscFolder);
        File ratesArchive = new File(miscFolder, dictFileName);

        if (ratesArchive.exists()) {
            try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(ratesArchive))) {
                CellGroup group = initCellGroup();
                Workbook wb = new HSSFWorkbook(in);

                processSheets(wb, group);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.format("Dictionary file %s can not be found in folder %s. Import operation is canceled", ratesArchive.getName(), miscFolder.getAbsolutePath());
        }
    }

    private void processSheets(Workbook wb, CellGroup group) {
        for (Map.Entry<Integer, List<CellCoordinate>> tableStartCellEntry : tableStartCells.entrySet()) {
            Sheet sheet = wb.getSheetAt(tableStartCellEntry.getKey());

            for (CellCoordinate coord : tableStartCellEntry.getValue()) {
                ItemContainer container = itemContainerFactory.createItemContainer(sheet, coord);
                ExcelImportRateDto dto = null;
                while (!isEmpty(dto)) {
                    dto = container.readItem(ExcelImportRateDto.class, group, messagesHolder);
                    if (!isEmpty(dto)) {
                        dtoConsumer.accept(dto);
                    }
                }
            }
        }
    }

    private boolean isEmpty(ExcelImportRateDto dto) {
        return dto != null && StringUtils.isBlank(dto.getCode()) && StringUtils.isBlank(dto.getName()) && StringUtils.isBlank(dto.getDesc());
    }

    @NotNull
    private CellGroup initCellGroup() {
        //Define row cell group
        CellGroup group = new CellGroup();
        for (Map.Entry<CellCoordinate, String> col : columns.entrySet()) {
            group.addCell(col.getKey(), new BeanPropertyValueReference(col.getValue()));
        }
        return group;
    }

    private Map<Integer, List<CellCoordinate>> initTableStartCells() {
        Map<Integer, List<CellCoordinate>> tableStartCells = new HashMap<>();
        tableStartCells.put(3, Arrays.asList(
                new CellCoordinate(2, 250),
                new CellCoordinate(2, 953),
                new CellCoordinate(2, 1634),
                new CellCoordinate(2, 1945)
        ));
        tableStartCells.put(4, Arrays.asList(
                new CellCoordinate(2, 16),
                new CellCoordinate(2, 95),
                new CellCoordinate(2, 299),
                new CellCoordinate(2, 508),
                new CellCoordinate(2, 632),
                new CellCoordinate(2, 774),
                new CellCoordinate(2, 923),
                new CellCoordinate(2, 1064),
                new CellCoordinate(2, 1096)
        ));
        tableStartCells.put(5, Arrays.asList(
                new CellCoordinate(2, 121)
        ));
        tableStartCells.put(6, Arrays.asList(
                new CellCoordinate(2, 23),
                new CellCoordinate(2, 207),
                new CellCoordinate(2, 397),
                new CellCoordinate(2, 461),
                new CellCoordinate(2, 589),
                new CellCoordinate(2, 1238),
                new CellCoordinate(2, 1393),
                new CellCoordinate(2, 1790),
                new CellCoordinate(2, 1903)
        ));
        tableStartCells.put(7, Arrays.asList(
                new CellCoordinate(2, 26),
                new CellCoordinate(2, 163),
                new CellCoordinate(2, 306)

        ));
        tableStartCells.put(9, Arrays.asList(
                new CellCoordinate(2, 95),
                new CellCoordinate(2, 510)
        ));
        tableStartCells.put(10, Arrays.asList(
                new CellCoordinate(2, 19),
                new CellCoordinate(2, 176),
                new CellCoordinate(2, 259)
        ));
        tableStartCells.put(11, Arrays.asList(
                new CellCoordinate(2, 45),
                new CellCoordinate(2, 386),
                new CellCoordinate(2, 423)
        ));
        tableStartCells.put(12, Arrays.asList(
                new CellCoordinate(2, 14),
                new CellCoordinate(2, 146),
                new CellCoordinate(2, 436)
        ));
        tableStartCells.put(13, Arrays.asList(
                new CellCoordinate(2, 144),
                new CellCoordinate(2, 194),
                new CellCoordinate(2, 307),
                new CellCoordinate(2, 538),
                new CellCoordinate(2, 607),
                new CellCoordinate(2, 745),
                new CellCoordinate(2, 979),
                new CellCoordinate(2, 1091),
                new CellCoordinate(2, 1173),
                new CellCoordinate(2, 1296),
                new CellCoordinate(2, 1378),
                new CellCoordinate(2, 1514),
                new CellCoordinate(2, 1794),
                new CellCoordinate(2, 2113)
        ));
        tableStartCells.put(14, Arrays.asList(
                new CellCoordinate(2, 34),
                new CellCoordinate(2, 185),
                new CellCoordinate(2, 227),
                new CellCoordinate(2, 261)
        ));
        tableStartCells.put(15, Arrays.asList(
                new CellCoordinate(2, 27),
                new CellCoordinate(2, 145),
                new CellCoordinate(2, 246)
        ));
        tableStartCells.put(16, Arrays.asList(
                new CellCoordinate(2, 54)
        ));
        tableStartCells.put(17, Arrays.asList(
                new CellCoordinate(2, 153),
                new CellCoordinate(2, 776),
                new CellCoordinate(2, 1371),
                new CellCoordinate(2, 1502),
                new CellCoordinate(2, 1576),
                new CellCoordinate(2, 1738),
                new CellCoordinate(2, 1777),
                new CellCoordinate(2, 1820),
                new CellCoordinate(2, 1840),
                new CellCoordinate(2, 1976),
                new CellCoordinate(2, 2144)
        ));
        tableStartCells.put(18, Arrays.asList(
                new CellCoordinate(2, 108),
                new CellCoordinate(2, 2076)
        ));
        tableStartCells.put(19, Arrays.asList(
                new CellCoordinate(2, 53),
                new CellCoordinate(2, 131),
                new CellCoordinate(2, 717),
                new CellCoordinate(2, 784)
        ));
        tableStartCells.put(20, Arrays.asList(
                new CellCoordinate(2, 40),
                new CellCoordinate(2, 517),
                new CellCoordinate(2, 612)
        ));
        tableStartCells.put(21, Arrays.asList(
                new CellCoordinate(2, 19)
        ));
        tableStartCells.put(22, Arrays.asList(
                new CellCoordinate(2, 33),
                new CellCoordinate(2, 255),
                new CellCoordinate(2, 382)
        ));
        tableStartCells.put(23, Arrays.asList(
                new CellCoordinate(2, 20)
        ));
        return tableStartCells;
    }

    public SimpleProcessMessagesHolder getMessagesHolder() {
        return messagesHolder;
    }
}
