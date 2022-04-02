package com.evans.view;

import com.evans.common.Utils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class JadSettingPanel extends JPanel {

    // 定义二维数组作为表格数据
    Object[][] tableData = {
            new Object[]{"rbr", ""},
            new Object[]{"rsy", ""},
            new Object[]{"din", ""},
            new Object[]{"dc4", ""},
            new Object[]{"das", ""},
            new Object[]{"hes", ""},
            new Object[]{"hdc", ""},
            new Object[]{"dgs", ""},
            new Object[]{"den", ""},
            new Object[]{"rgn", ""},
            new Object[]{"asc", ""},
            new Object[]{"bto", ""},
            new Object[]{"nns", ""},
            new Object[]{"udv", ""},
            new Object[]{"ump", ""},
            new Object[]{"rer", ""},
            new Object[]{"fdi", ""},
            new Object[]{"mpm", ""},
            new Object[]{"ren", ""},
            new Object[]{"urc", ""},
            new Object[]{"inn", ""},
            new Object[]{"lac", ""},
            new Object[]{"nls", ""},
            new Object[]{"ind", ""},
            new Object[]{"log", ""},
            new Object[]{"e", ""}
    };

    //定义一个一维数组，作为列标题
    Object[] tableHeader = {"Key", "Value"};

    JTable table;

    String description = "Key options\n" +
            "With the exception of mpm and urc the value of 1 means the option is activated, 0 - deactivated. Default value, if any, is given between parentheses.\n" +
            "\n" +
            "Typically, the following options will be changed by user, if any: hes, hdc, dgs, mpm, ren, urc The rest of options can be left as they are: they are aimed at professional reverse engineers.\n" +
            "\n" +
            "  ·rbr (1): hide bridge methods\n" +
            "  ·rsy (0): hide synthetic class members\n" +
            "  ·din (1): decompile inner classes\n" +
            "  ·dc4 (1): collapse 1.4 class references\n" +
            "  ·das (1): decompile assertions\n" +
            "  ·hes (1): hide empty super invocation\n" +
            "  ·hdc (1): hide empty default constructor\n" +
            "  ·dgs (0): decompile generic signatures\n" +
            "  ·ner (1): assume return not throwing exceptions\n" +
            "  ·den (1): decompile enumerations\n" +
            "  ·rgn (1): remove getClass() invocation, when it is part of a qualified new statement\n" +
            "  ·lit (0): output numeric literals \"as-is\"\n" +
            "  ·asc (0): encode non-ASCII characters in string and character literals as Unicode escapes\n" +
            "  ·bto (1): interpret int 1 as boolean true (workaround to a compiler bug)\n" +
            "  ·nns (0): allow for not set synthetic attribute (workaround to a compiler bug)\n" +
            "  ·uto (1): consider nameless types as java.lang.Object (workaround to a compiler architecture flaw)\n" +
            "  ·udv (1): reconstruct variable names from debug information, if present\n" +
            "  ·ump (1): reconstruct parameter names from corresponding attributes, if present\n" +
            "  ·rer (1): remove empty exception ranges\n" +
            "  ·fdi (1): de-inline finally structures\n" +
            "  ·mpm (0): maximum allowed processing time per decompiled method, in seconds. 0 means no upper limit\n" +
            "  ·ren (0): rename ambiguous (resp. obfuscated) classes and class elements\n" +
            "  ·urc (-): full name of a user-supplied class implementing IIdentifierRenamer interface. It is used to determine which class identifiers should be renamed and provides new identifier names (see \"Renaming identifiers\")\n" +
            "  ·inn (1): check for IntelliJ IDEA-specific @NotNull annotation and remove inserted code if found\n" +
            "  ·lac (0): decompile lambda expressions to anonymous classes\n" +
            "  ·nls (0): define new line character to be used for output. 0 - '\\r\\n' (Windows), 1 - '\\n' (Unix), default is OS-dependent\n" +
            "  ·ind: indentation string (default is 3 spaces)\n" +
            "  ·log (INFO): a logging level, possible values are TRACE, INFO, WARN, ERROR\n" +
            "Renaming identifiers\n" +
            "Some obfuscators give classes and their member elements short, meaningless and above all ambiguous names. Recompiling of such code leads to a great number of conflicts. Therefore it is advisable to let the decompiler rename elements in its turn, ensuring uniqueness of each identifier.\n" +
            "\n" +
            "Option 'ren' (i.e. -ren=1) activates renaming functionality. Default renaming strategy goes as follows:\n" +
            "\n" +
            "  ·rename an element if its name is a reserved word or is shorter than 3 characters\n" +
            "  ·new names are built according to a simple pattern: (class|method|field)_<consecutive unique number>\n" +
            "You can overwrite this rules by providing your own implementation of the 4 key methods invoked by the decompiler while renaming. Simply pass a class that implements org.jetbrains.java.decompiler.main.extern.IIdentifierRenamer in the option 'urc' (e.g. -urc=com.example.MyRenamer) to Fernflower. The class must be available on the application classpath.\n" +
            "The meaning of each method should be clear from naming: toBeRenamed determine whether the element will be renamed, while the other three provide new names for classes, methods and fields respectively.";

    JTextArea descriptionTextArea = new JTextArea(description);

    public void init() {
        DefaultTableModel tableModel = new DefaultTableModel(tableData, tableHeader) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0) {
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };

        table = new JTable(tableModel);

        JScrollPane tableScrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);


        // 设置不可编辑
        descriptionTextArea.setEditable(false);
        // 设置自动换行
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setColumns(60);
        descriptionTextArea.setRows(40);

        JScrollPane descriptionScrollPane = new JScrollPane(descriptionTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


        Box box = Box.createHorizontalBox();
        box.add(tableScrollPane);
        box.add(Box.createHorizontalGlue());
        box.add(descriptionScrollPane);


        this.add(box);
    }

    // 获取界面参数
    public List<String> getOptionsMap() {
        List<String> optionsMap = new ArrayList<>();
        for (int i=0; i<table.getRowCount(); i++) {
            String key = (String) table.getValueAt(i,0);
            Object value = table.getValueAt(i, 1);

            if (null != value && !Utils.isEmpty((String) value)) {
                optionsMap.add("-" + key + "=" + value);
            }
        }
        return optionsMap;
    }


}
