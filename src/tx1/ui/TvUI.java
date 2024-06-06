package tx1.ui;

import tx1.TV;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class TvUI extends JFrame {
    private JPanel rootPanel;
    private JTable showTable;
    private JButton btnAdd;
    private JButton btnSearch;
    private JButton btnSort;
    private JButton btnRef;
    private JButton btnDelete;
    private JButton btnEdit;
    protected static List<TV> tvList = new ArrayList<>();

    public TvUI() {

        createTable();
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnAdd.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        new AddEditDialog(TvUI.this, "Thêm mới", null, -1).setVisible(true);
                    }
                });
            }
        });
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = JOptionPane.showInputDialog("Enter product name to search:");
                if (searchTerm != null && !searchTerm.isEmpty()) {
                    DefaultTableModel model = (DefaultTableModel) showTable.getModel();
                    List<Object[]> filteredRows = new ArrayList<>();
                    for (int i = 0; i < model.getRowCount(); i++) {
                        String productName = model.getValueAt(i, 1).toString();
                        if (productName.contains(searchTerm)) {
                            Vector<Object> rowData = model.getDataVector().elementAt(i);
                            Object[] rowArray = new Object[((Vector<?>) rowData).size()];
                            rowData.toArray(rowArray);
                            filteredRows.add(rowArray);
                        }
                    }
                    Object[][] filteredData = new Object[filteredRows.size()][model.getColumnCount()];
                    filteredRows.toArray(filteredData);
                    showTable.setModel(new DefaultTableModel(
                            filteredData,
                            new String[] {"Product ID", "Product Name","Product Price", "Product Total", "Screen Size", "Resolution", "Smart TV"}
                    ));
                }

            }
        });
        btnSort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableRowSorter<TableModel> sorter = new TableRowSorter<>(showTable.getModel());
                showTable.setRowSorter(sorter);
                List<RowSorter.SortKey> sortKeys = new ArrayList<>();
                sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));
                sortKeys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));
                sorter.setSortKeys(sortKeys);
                sorter.sort();
            }
        });


        btnRef.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createTable();
            }
        });
    }
    public void addProduct(Object[] product) {
        DefaultTableModel model = (DefaultTableModel) showTable.getModel();
        model.addRow(product);

    }


    public void updateProduct(int rowIndex, Object[] product) {
        for (int i = 0; i < product.length; i++) {
            showTable.setValueAt(product[i], rowIndex, i);
        }

    }
    public JPanel getRootPanel() {
        return rootPanel;
    }
    private void createTable() {
        loadFromFile();
        Object[][] data = new Object[tvList.size()][];
        for (int i = 0; i < tvList.size(); i++) {
            TV tv = tvList.get(i);
            // Chuyển đổi từng đối tượng TV thành một mảng Object
            Object[] rowData = {
                    tv.getProduct_id(),
                    tv.getProduct_name(),
                    tv.getProduct_price(),
                    tv.getProduct_total(),
                    tv.getScreenSize(),
                    tv.getResolution(),
                    tv.isSmartTV()
            };
            data[i] = rowData;
        }
        showTable.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[] {"Product ID", "Product Name","Product Price", "Product Total", "Screen Size", "Resolution", "Smart TV"}
        ));


        // Add mouse listener to show popup menu
        showTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopup(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopup(e);
                }
            }

            private void showPopup(MouseEvent e) {
                int row = showTable.rowAtPoint(e.getPoint());
                showTable.setRowSelectionInterval(row, row);
                JPopupMenu popupMenu = createPopupMenu(row);
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }

    private JPopupMenu createPopupMenu(int row) {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem editItem = new JMenuItem("Sửa");
        editItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editRow(row);
            }
        });
        popupMenu.add(editItem);

        JMenuItem deleteItem = new JMenuItem("Xóa");
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRow(row);
                tvList.remove(row);
                saveToFile();
            }
        });
        popupMenu.add(deleteItem);

        return popupMenu;
    }

    private void editRow(int row) {
        Object[] product = new Object[showTable.getColumnCount()];
        for (int i = 0; i < showTable.getColumnCount(); i++) {
            product[i] = showTable.getValueAt(row, i);
        }
        new AddEditDialog(this, "Edit Product", product, row).setVisible(true);
    }

    private void deleteRow(int row) {
        ((DefaultTableModel) showTable.getModel()).removeRow(row);
    }

    public static void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("TV.bin"))) {
            oos.writeObject(tvList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load tvList from a file
    public static void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("TV.bin"))) {
            tvList = (List<TV>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
