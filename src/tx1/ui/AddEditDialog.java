package tx1.ui;


import tx1.TV;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static tx1.ui.TvUI.saveToFile;
import static tx1.ui.TvUI.tvList;

class AddEditDialog extends JDialog {
    private JTextField txtProductId, txtProductName, txtProductPrice, txtProductTotal, txtScreenSize, txtResolution, txtIsSmartTV;
    private boolean isEdit;
    private int rowIndex;
    private TvUI parent;
    private JPanel panel1;

    public AddEditDialog(JFrame parent, String title, Object[] product, int rowIndex) {
        super(parent, title, true);
        this.parent = (TvUI) parent;
        this.rowIndex = rowIndex;
        setLayout(new GridLayout(8, 2));
        setSize(400, 300);
        setLocationRelativeTo(parent);

        int margin = 10; // Margin 10 pixel

        // Khởi tạo các JTextField
        txtProductId = new JTextField();
        txtProductName = new JTextField();
        txtProductPrice = new JTextField();
        txtProductTotal = new JTextField();
        txtScreenSize = new JTextField();
        txtResolution = new JTextField();
        txtIsSmartTV = new JTextField();

        add(createLabeledTextField("Product Id", txtProductId, margin));
        add(createLabeledTextField("Product Name", txtProductName, margin));
        add(createLabeledTextField("Product Price", txtProductPrice, margin));
        add(createLabeledTextField("Product Total", txtProductTotal, margin));
        add(createLabeledTextField("Screen Size", txtScreenSize, margin));
        add(createLabeledTextField("Resolution", txtResolution, margin));
        add(createLabeledTextField("Is Smart TV?", txtIsSmartTV, margin));

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                    if (isEdit) {
                        ((TvUI) parent).updateProduct(rowIndex, getProductData());
                        updateFile(rowIndex);

                    } else {
                        if (validateForm()) ((TvUI) parent).addProduct(getProductData());
                        saveFile();
                    }
                     // Lưu dữ liệu vào tệp tin
                    dispose(); // Đóng cửa sổ sau khi lưu

            }
        });
        add(btnSave);

        if (product != null) {
            isEdit = true;
            setProductData(product);
        }
    }

    private boolean validateForm() {
        for (int i = 0; i < tvList.size(); i++) {
            if (txtProductId.getText().equalsIgnoreCase(tvList.get(i).getProduct_id())) {
                JOptionPane.showMessageDialog(parent, "Duplicate product ID");
                return false;
            }
        }
        return true;
    }

    private void saveFile() {
        TV tv = new TV();
        tv.setProduct_id((String) getProductData()[0]);
        tv.setProduct_name((String) getProductData()[1]);
        tv.setProduct_price((Double) getProductData()[2]);
        tv.setProduct_total(((Integer) getProductData()[3]));
        tv.setScreenSize((String) getProductData()[4]);
        tv.setResolution((String) getProductData()[5]);
        tv.setSmartTV(getProductData()[6].toString().equalsIgnoreCase("true"));
        tvList.add(tv);
        saveToFile();
    }

    private void updateFile(int rowIndex){
        if (rowIndex >= 0 && rowIndex < tvList.size()) {
            TV tv = tvList.get(rowIndex);
            tv.setProduct_id((String) getProductData()[0]);
            tv.setProduct_name((String) getProductData()[1]);
            tv.setProduct_price((Double) getProductData()[2]);
            tv.setProduct_total((Integer) getProductData()[3]);
            tv.setScreenSize((String) getProductData()[4]);
            tv.setResolution((String) getProductData()[5]);
            tv.setSmartTV(getProductData()[6].toString().equalsIgnoreCase("true"));
            tvList.set(rowIndex, tv);
            saveToFile();
        } else {
            throw new IndexOutOfBoundsException("Index " + rowIndex + " out of bounds for length " + tvList.size());
        }
    }

    private Object[] getProductData() {
        return new Object[]{txtProductId.getText(), txtProductName.getText(), Double.parseDouble(txtProductPrice.getText()), Integer.parseInt(txtProductTotal.getText()), txtScreenSize.getText(), txtResolution.getText(), txtIsSmartTV.getText()};
    }

    private void setProductData(Object[] product) {
        txtProductId.setText(product[0].toString());
        txtProductName.setText(product[1].toString());
        txtProductPrice.setText(product[2].toString());
        txtProductTotal.setText(product[3].toString());
        txtScreenSize.setText(product[4].toString());
        txtResolution.setText(product[5].toString());
        txtIsSmartTV.setText(product[6].toString());
    }

    private JPanel createLabeledTextField(String labelText, JTextField textField, int margin) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(labelText);
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, margin)); // Margin bên phải cho nhãn
        panel.add(label, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);
        return panel;
    }

}

