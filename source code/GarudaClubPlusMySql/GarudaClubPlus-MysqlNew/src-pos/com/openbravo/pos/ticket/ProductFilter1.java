/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ProductFilter1.java
 *
 * Created on Feb 22, 2009, 12:21:38 PM
 */

package com.openbravo.pos.ticket;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.ListQBFModelNumber;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.QBFCompareEnum;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerWrite;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.reports.ReportEditorCreator;
import java.awt.Component;
import java.util.List;

/**
 *
 * @author swathi
 */
public class ProductFilter1 extends javax.swing.JPanel implements ReportEditorCreator {

    /** Creates new form ProductFilter1 */
     private SentenceList m_sentcat;
    private ComboBoxValModel m_CategoryModel;

    /** Creates new form JQBFProduct */
    public ProductFilter1() {

        initComponents();
    }

    public void init(AppView app) {

        DataLogicSales dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");

        // El modelo de categorias
        m_sentcat = dlSales.getprCategoriesList();
        m_CategoryModel = new ComboBoxValModel();

        m_jCboName.setModel(new ListQBFModelNumber());
        m_jCboPriceBuy.setModel(new ListQBFModelNumber());
        m_jCboPriceSell.setModel(new ListQBFModelNumber());
    }

    public void activate() throws BasicException {

        List catlist = m_sentcat.list();
        catlist.add(0, null);
        m_CategoryModel = new ComboBoxValModel(catlist);
        m_jCategory.setModel(m_CategoryModel);
    }

    public SerializerWrite getSerializerWrite() {
        return new SerializerWriteBasic(
                new Datas[] {Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING});
    }

    public Component getComponent() {
        return this;
    }

    public Object createValue() throws BasicException {

        if (m_jBarcode.getText() == null || m_jBarcode.getText().equals("")) {
            // Filtro por formulario
            return new Object[] {
                m_jCboName.getSelectedItem(), m_jName.getText(),
                m_jCboPriceBuy.getSelectedItem(), Formats.CURRENCY.parseValue(m_jPriceBuy.getText()),
                m_jCboPriceSell.getSelectedItem(), Formats.CURRENCY.parseValue(m_jPriceSell.getText()),
                m_CategoryModel.getSelectedKey() == null ? QBFCompareEnum.COMP_NONE : QBFCompareEnum.COMP_EQUALS, m_CategoryModel.getSelectedKey(),
                QBFCompareEnum.COMP_NONE, null
            };
        } else {
            // Filtro por codigo de barras.
            return new Object[] {
                QBFCompareEnum.COMP_NONE, null,
                QBFCompareEnum.COMP_NONE, null,
                QBFCompareEnum.COMP_NONE, null,
                QBFCompareEnum.COMP_NONE, null,
                QBFCompareEnum.COMP_RE, "%" + m_jBarcode.getText() + "%"
            };
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        m_jCategory = new javax.swing.JComboBox();
        m_jCboPriceSell = new javax.swing.JComboBox();
        m_jCboPriceBuy = new javax.swing.JComboBox();
        m_jCboName = new javax.swing.JComboBox();
        m_jName = new javax.swing.JTextField();
        m_jPriceBuy = new javax.swing.JTextField();
        m_jPriceSell = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        m_jBarcode = new javax.swing.JTextField();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("By form"));

        jLabel1.setText("Print Category");

        jLabel2.setText("Name");

        jLabel3.setText("Buy Price");

        jLabel4.setText("Sell Price");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(m_jCboPriceBuy, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(m_jCboName, 0, 167, Short.MAX_VALUE)
                            .addComponent(m_jCboPriceSell, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(m_jName, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(m_jPriceSell, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(m_jPriceBuy, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE))))
                    .addComponent(m_jCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(86, 86, 86))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(m_jCboName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(m_jCboPriceBuy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jPriceBuy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(m_jCboPriceSell, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jPriceSell, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(m_jCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("By barcode"));

        jLabel5.setText("Barcode");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59)
                .addComponent(m_jBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(304, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(m_jBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 638, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField m_jBarcode;
    private javax.swing.JComboBox m_jCategory;
    private javax.swing.JComboBox m_jCboName;
    private javax.swing.JComboBox m_jCboPriceBuy;
    private javax.swing.JComboBox m_jCboPriceSell;
    private javax.swing.JTextField m_jName;
    private javax.swing.JTextField m_jPriceBuy;
    private javax.swing.JTextField m_jPriceSell;
    // End of variables declaration//GEN-END:variables

}
