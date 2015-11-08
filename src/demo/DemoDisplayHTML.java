package demo; /**
 * Created by cuong on 10/16/2015.
 */
import javax.swing.JFrame;
import javax.swing.JLabel;

public class DemoDisplayHTML extends JFrame {

    private String data = "a	<i>a /ei, ə/</i><br/><ul><li><b><i> danh từ, số nhiều as, a's</i></b><ul><li><font color='#cc0000'><b> (thông tục) loại a, hạng nhất, hạng tốt nhất hạng rất tốt</b></font><ul><li><b>his health is a</b>: sức khoẻ anh ta vào loại a</li></ul></li></ul><ul><li><font color='#cc0000'><b> (âm nhạc) la</b></font><ul><li><b>a sharp</b>: la thăng</li><li><b>a flat</b>: la giáng</li></ul></li></ul><ul><li><font color='#cc0000'><b> người giả định thứ nhất; trường hợp giả định thứ nhất</b></font><ul><li><b>from a to z</b>: từ đầu đến đuôi, tường tận</li><li><b>not to know a from b</b>: không biết tí gì cả; một chữ bẻ đôi cũng không biết</li></li></ul></ul></li></ul><ul><li><b><i> mạo từ</i></b><ul><li><font color='#cc0000'><b> một; một (như kiểu); một (nào đó)</b></font><ul><li><b>a very cold day</b>: một ngày rất lạnh</li><li><b>a dozen</b>: một tá</li><li><b>a few</b>: một ít</li><li><b>all of a size</b>: tất cả cùng một cỡ</li><li><b>a Shakespeare</b>: một (văn hào như kiểu) Sếch-xpia</li><li><b>a Mr Nam</b>: một ông Nam (nào đó)</li></ul></li></ul><ul><li><font color='#cc0000'><b> cái, con, chiếc, cuốn, người, đứa...;</b></font><ul><li><b>a cup</b>: cái chén</li><li><b>a knife</b>: con dao</li><li><b>a son of the Party</b>: người con của Đảng</li><li><b>a Vietnamese grammar</b>: cuốn ngữ pháp Việt Nam</li></li></ul></ul></li></ul><ul><li><b><i> giới từ</i></b><ul><li><font color='#cc0000'><b> mỗi, mỗi một</b></font><ul><li><b>twice a week</b>: mỗi tuần hai lần</li></ul></li></ul></li></ul>";
    private JLabel label;

    public DemoDisplayHTML() {
        label = new JLabel();
        int i = data.indexOf("<i>");
        data = data.substring(0, i-1) + "<br/>" + data.substring(i);
        data = "<html>" + data + "</html>";
        label.setText(data);
        this.add(label);
    }

    public static void main(String[] args) {

        DemoDisplayHTML f = new DemoDisplayHTML();

        f.setSize(400, 400);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();

    }

}

