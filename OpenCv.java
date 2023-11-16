/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javafx.scene.image.Image;

/**
 *
 * @author hinh
 */

public class OpenCv extends javax.swing.JFrame {

    /**
     * Creates new form NewJFrame1
     */
    private Thread captureThread;
    public OpenCv() {
        initComponents();
        captureThread();
        myThread = new DaemonThread(jPanel2); // truyền jPanel2 vào DaemonThread
        Thread t = new Thread(myThread);
        t.setDaemon(true);
        myThread.runnable = true;
        t.start();
    }
    static{
        File file = new File("D:/opencv/build/java/x64/opencv_java249.dll");
        System.load(file.getAbsolutePath());
    }


    @SuppressWarnings("unchecked")
    private DaemonThread myThread = null;
    private VideoCapture webSource = new VideoCapture(0);;
    private final Mat frame = new Mat(640, 480, 1);
    private final MatOfByte mem = new MatOfByte();
    private class DaemonThread implements Runnable {
        protected volatile boolean runnable = false;
        private JPanel display; 
        public DaemonThread(JPanel jPanel) {
            this.display = jPanel;
        }
        @Override
        public void run() {
            synchronized (this) {
                while (runnable) {
                    if (webSource.grab()) {
                        try {
                            webSource.retrieve(frame);
                            Highgui.imencode(".bmp", frame, mem);
                            BufferedImage im = ImageIO.read(new ByteArrayInputStream(mem.toArray()));
                            BufferedImage buff = (BufferedImage) im;
                            Graphics g = display.getGraphics();
                            if (g.drawImage(buff, 1, 1, display.getWidth(), jPanel2.getHeight(), null)) {
                                if (runnable == false) {
                                    this.wait();
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            } 
        }
    }

    public void captureThread() {
        captureThread = new Thread(() -> {
            // Chụp ảnh liên tục
            while (true) {
                try {
                     // để sau mỗi 500ml giây mới tiến hành chụp
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                if (webSource.grab()) {
                    try {
                        // Lấy hình ảnh từ webcam và chuyển đổi thành BufferedImage
                        webSource.retrieve(frame);
                        BufferedImage im = ImageIO.read(new ByteArrayInputStream(mem.toArray()));
                        BufferedImage buff = (BufferedImage) im;
    
                        // Gửi hình ảnh đến đối tượng BufferedImageLuminanceSource
                        LuminanceSource source = new BufferedImageLuminanceSource(buff);
                        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
    
                        // Giải mã QR code
                        Result result = new MultiFormatReader().decode(bitmap);
    
                        if (result != null) {
                            // Hiển thị thông báo với nội dung của mã vạch
                           //list1.add(result.getText());
                           //JOptionPane.showMessageDialog(OpenCv.this, "Ma qr la :" +result.getText());
                           System.out.println(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally{
                        frame.release();

                    }
                    
                    
                }
            }
        });
    
        // Đặt thread này là một Daemon thread và chạy thread
        captureThread.setDaemon(true);
        captureThread.start();
    }
    
       void stopWebcam(){
        if(myThread !=null){
        if(myThread.runnable ==true){
            myThread.runnable = false ;
            webSource.release();
        }
       }
    }  
                
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
        private void initComponents() {

            jPanel1 = new javax.swing.JPanel();
            jPanel2 = new javax.swing.JPanel();
            jButton1 = new javax.swing.JButton();
            jButton2 = new javax.swing.JButton();
            list1 = new java.awt.List();
    
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    
            jPanel1.setBackground(new java.awt.Color(204, 204, 255));
    
            javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 1016, Short.MAX_VALUE)
            );
            jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 409, Short.MAX_VALUE)
            );
    
            jButton1.setText("Tắt Camera");
            jButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });
    
            jButton2.setText("End");
            jButton2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton2ActionPerformed(evt);
                }
            });
    
            list1.setBackground(new java.awt.Color(204, 204, 204));
    
            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(60, 60, 60)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 535, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(18, 18, 18)
                    .addComponent(list1, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                    .addContainerGap())
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(40, 40, 40)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGap(18, 18, 18))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(list1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );
    
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );
    
            pack();
        }// </editor-fold>                   

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {         
        stopWebcam();
        remove(jPanel2);
        jPanel2.revalidate();
        jPanel2.repaint();
    }                               
        

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
      dispose();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(OpenCv.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OpenCv.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OpenCv.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OpenCv.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OpenCv().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JPanel jPanel1;
    private JPanel jPanel2;
    private java.awt.List list1;

    // End of variables declaration                   
}
