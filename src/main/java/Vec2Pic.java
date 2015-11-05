import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;

import javax.imageio.ImageIO;
/**
 * Created by zhang on 2015/10/29.
 */
public class Vec2Pic {

    public static void createMatrixImage(int[][] matrix, String filedir)
            throws IOException {
        int cy = matrix.length;
        int cx = matrix[0].length;
        int width = cx;
        int height = cy;

        OutputStream output = new FileOutputStream(new File(filedir));
        BufferedImage bufImg = new BufferedImage(width, height,
                BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D gs = bufImg.createGraphics();
        gs.clearRect(0, 0, width, height);

        for (int i = 0; i < cx; i++) {
            for (int j = 0; j < cy; j++) {
                if (matrix[j][i] == 1) {          //arable land
                    gs.setColor(new Color(100,100, 100));
                } else if (matrix[j][i] == 2) {   //background
                    gs.setColor(new Color(0, 0, 0));
                } else if (matrix[j][i] == 3) {   //urban
                    gs.setColor(new Color(200, 200, 200));
                } else if (matrix[j][i] == 4) {   //vegetation
                    gs.setColor(new Color(50, 50, 50));
                } else {                            //water
                    gs.setColor(new Color(150, 150, 150));
                }
                gs.drawOval(i, j, 1, 1);
            }
        }
        gs.dispose();
        bufImg.flush();
        // ����ļ�
        ImageIO.write(bufImg, "jpeg", output);

    }

    public static int[][] read(){
        int result[][] =  new int[5427][5353];
        try {
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(new URI("hdfs://10.214.0.151:8020"), conf);
            int i = 0, j = 0, k = 0;
            for(int x = 0; x < 3; x++){
                Path file = new Path("/user/syzh/gaofen/classification/out/part-0000" + x);
                FSDataInputStream getIt = fs.open(file);
                BufferedReader d = new BufferedReader(new InputStreamReader(getIt));
                String s = "";
                while ((s = d.readLine()) != null) {
                    if((i+1)%5427 != 0){
                        result[j][k] = (int)Double.parseDouble(s);
                        j++;
                    }
                    else{
                        result[j][k] = (int)Double.parseDouble(s);
                        k++;
                        j=0;
                    }
                    i++;
                }
                d.close();
            }
            fs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        int[][] matrix = read();
        try {
            Vec2Pic.createMatrixImage(matrix, "test.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
