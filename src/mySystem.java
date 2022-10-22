import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;

public class mySystem {
    public static void main(String[] args) {
        myJframe frame = new myJframe("模拟先来先服务调度算法");
        cenPanel cenpanel = new cenPanel();
        frame.add(cenpanel);
        myThread1 th1 = new myThread1(cenpanel);
        myThread2 th2 = new myThread2(cenpanel);
        myThread3 th3 = new myThread3(cenpanel);
        myThread4 th4 = new myThread4(cenpanel);
        CpuThread cpu = new CpuThread(cenpanel);
        while (true) {
            if (myThread1.Flag == 1) {
                th1.run();
            } else {
                th1.yield();
            }
            if (myThread2.Flag == 1) {
                th2.run();
            } else {
                th2.yield();
            }
            if (myThread3.Flag == 1) {
                th3.run();
            } else {
                th3.yield();
            }
            if (myThread4.Flag == 1) {
                th4.run();
            } else {
                th4.yield();
            }
            if (CpuThread.flag == 1) {
                cpu.run();
            } else {
                cpu.yield();
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}

class cenPanel extends JPanel {

    Cpu cpu = new Cpu();
    waitArea wait = new waitArea();
    JButton btn1 = new JButton("开始");
    JButton btn2 = new JButton("重置");
    JLabel[] label = new JLabel[4];
    JLabel cenlabel = new JLabel();
    static int g1 = 75;
    static int g2 = 75;
    static int g3 = 75;
    static int g4 = 75;
    static int[] speed = new int[4];

    public cenPanel() {
        setBackground(new Color(0xCEEFF5));
        setBounds(0, 0, 1300, 900);
        setLayout(null);
        Font f = new Font("仿宋", Font.BOLD, 21);
        //Label
        for (int i = 0; i < 4; i++) {
            int n = i + 1;
            label[i] = new JLabel("进程" + n);
            label[i].setFont(f);
            label[i].setBounds(79, 165 + i * 150, 60, 30);
            add(label[i]);
        }
        Font f2 =new Font("仿宋",Font.BOLD,40);
        cenlabel.setFont(f2);
        cenlabel.setBounds(100,400,400,100);
        add(cenlabel);
        cenlabel.setVisible(false);
        //Button
        btn1.setBounds(50, 50, 150, 80);
        btn1.setFont(f);
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myThread1.Flag = 1;
                myThread2.Flag = 1;
                myThread3.Flag = 1;
                myThread4.Flag = 1;
            }
        });
        btn2.setBounds(250, 50, 150, 80);
        btn2.setFont(f);
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myThread1.Flag = 0;
                myThread2.Flag = 0;
                myThread3.Flag = 0;
                myThread4.Flag = 0;
                g1 = 75;
                g2 = 75;
                g3 = 75;
                g4 = 75;
                repaint();
                for (int i = 0; i < 4; i++) {
                    label[i].setBounds(79, 165 + i * 150, 60, 30);
                }
                CpuThread.flag = 0;
                cenlabel.setVisible(false);
                Random r= new Random();
                for (int i=0;i<4;i++){
                    speed[i]=r.nextInt(4)+3;
                    for (int n=0;n<i;n++){
                        if (speed[i]==speed[n]){
                            i--;
                        }
                    }
                }
                while (CpuThread.queue.peek()!=null){
                    CpuThread.queue.poll();
                }
            }
        });
        add(btn1);
        add(btn2);
        add(cpu);
        add(wait);
        Random r= new Random();
        for (int i=0;i<4;i++){
            speed[i]=r.nextInt(4)+3;
            for (int n=0;n<i;n++){
                if (speed[i]==speed[n]){
                    i--;
                }
            }
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(new Color(0xF8D9B4));
        g.fillOval(g1, 200, 65, 65);
        g.setColor(new Color(0xEEAB75));
        g.fillOval(g2, 350, 65, 65);
        g.setColor(new Color(0xE88C32));
        g.fillOval(g3, 500, 65, 65);
        g.setColor(new Color(0xE0730A));
        g.fillOval(g4, 650, 65, 65);
    }
}

class Cpu extends JPanel {

    public static int Flag = 0;
    JLabel label = new JLabel("C P U");

    public Cpu() {
        setBackground(new Color(0xFFEAEEF1, true));
        setBounds(850, 100, 300, 700);
//        setLayout(null);
        Font f = new Font("仿宋", Font.BOLD, 30);
        label.setFont(f);
//        label.setForeground(new Color(0xFFFFFF));//设置字体颜色
        label.setBounds(108, 20, 100, 35);
        add(label);
    }
}

class waitArea extends JPanel {
    JLabel label = new JLabel("等待队列");

    public waitArea() {
        setBackground(new Color(0xB2BFC3));
        setBounds(770, 100, 80, 700);
        Font f = new Font("仿宋", Font.BOLD, 20);
        label.setFont(f);
        add(label);
    }
}

class myThread1 extends Thread {
    cenPanel cenpanel;
    public static int Flag = 0;//开关flag

    public myThread1(cenPanel cenpanel) {
        this.cenpanel = cenpanel;
    }

    public void run() {
        int x1 = cenpanel.label[0].getX();
        if (cenPanel.g1 < 775) {
            x1 = x1 + cenPanel.speed[0];
            cenPanel.g1 = cenPanel.g1 + cenPanel.speed[0];
            cenpanel.label[0].setLocation(x1, 165);
        } else {
            Flag = 0;
//            if (Cpu.Flag == 0) {
//                Cpu.Flag = 1;
//                cenPanel.flag1 = 1;
//            }
//            if (cenPanel.flag1 == 1) {
//                if (cenPanel.g1 < 1085) {
//                    x1 = x1 + 3;
//                    cenPanel.g1 = cenPanel.g1 + 3;
//                    cenpanel.label[0].setLocation(x1, 165);
////                    cenpanel.g.fillOval(cenPanel.g1, 200, 65, 65);
//                } else {
//                    Cpu.Flag = 0;
//                    cenPanel.flag1 = 0;
//                }
//            }
            CpuThread.queue.add(1);
            CpuThread.flag = 1;
        }
        cenpanel.repaint();
    }
}

class myThread2 extends Thread {
    cenPanel cenpanel;
    public static int Flag = 0;//开关flag

    public myThread2(cenPanel cenpanel) {
        this.cenpanel = cenpanel;
    }

    public void run() {
        int x2 = cenpanel.label[1].getX();
        if (cenPanel.g2 < 775) {
            x2 = x2 + cenPanel.speed[1];
            cenPanel.g2 = cenPanel.g2 + cenPanel.speed[1];
            cenpanel.label[1].setLocation(x2, 315);
        } else {
            Flag = 0;
//            if (Cpu.Flag == 0) {
//                Cpu.Flag = 1;
//                cenPanel.flag2 = 1;
//            }
//            if (cenPanel.flag2 == 1) {
//                if (cenPanel.g2 < 1085) {
//                    x2 = x2 + 3;
//                    cenPanel.g2 = cenPanel.g2 + 3;
//                    cenpanel.label[1].setLocation(x2, 315);
//                    cenpanel.g.fillOval(cenPanel.g2, 350, 65, 65);
//                } else {
//                    Cpu.Flag = 0;
//                    cenPanel.flag2 = 0;
//                }
//            }
            CpuThread.queue.add(2);
            CpuThread.flag = 1;
        }
        cenpanel.repaint();
    }
}

class myThread3 extends Thread {
    cenPanel cenpanel;
    public static int Flag = 0;//开关flag

    public myThread3(cenPanel cenpanel) {
        this.cenpanel = cenpanel;
    }

    public void run() {
        int x3 = cenpanel.label[2].getX();
        if (cenPanel.g3 < 775) {
            x3 = x3 + cenPanel.speed[2];
            cenPanel.g3 = cenPanel.g3 + cenPanel.speed[2];
            cenpanel.label[2].setLocation(x3, 465);
        } else {
            Flag = 0;
//            if (Cpu.Flag == 0) {
//                Cpu.Flag = 1;
//                cenPanel.flag3 = 1;
//            }
//            if (cenPanel.flag3 == 1) {
//                if (cenPanel.g3 < 1085) {
//                    x3 = x3 + 3;
//                    cenPanel.g3 = cenPanel.g3 + 3;
//                    cenpanel.label[2].setLocation(x3, 465);
//                    cenpanel.g.fillOval(cenPanel.g3, 500, 65, 65);
//                } else {
//                    Cpu.Flag = 0;
//                    cenPanel.flag3 = 0;
//                }
//            }
            CpuThread.queue.add(3);
            CpuThread.flag = 1;
        }
        cenpanel.repaint();
    }
}


class myThread4 extends Thread {
    cenPanel cenpanel;
    public static int Flag = 0;//开关flag

    public myThread4(cenPanel cenpanel) {
        this.cenpanel = cenpanel;
    }

    public void run() {
        int x4 = cenpanel.label[3].getX();
        if (cenPanel.g4 < 775) {
            x4 = x4 + cenPanel.speed[3];
            cenPanel.g4 = cenPanel.g4 + cenPanel.speed[3];
            cenpanel.label[3].setLocation(x4, 615);
        } else {
            Flag = 0;
//            if (Cpu.Flag == 0) {
//                Cpu.Flag = 1;
//                cenPanel.flag4 = 1;
//            }
//            if (cenPanel.flag4 == 1) {
//                if (cenPanel.g4 < 1085) {
//                    x4 = x4 + 3;
//                    cenPanel.g4 = cenPanel.g4 + 3;
//                    cenpanel.label[3].setLocation(x4, 615);
//                    cenpanel.g.fillOval(cenPanel.g4, 650, 65, 65);
//                } else {
//                    Cpu.Flag = 0;
//                    cenPanel.flag4 = 0;
//                }
//            }
            CpuThread.queue.add(4);
            CpuThread.flag = 1;
        }
        cenpanel.repaint();
    }
}

class myJframe extends JFrame {

    public myJframe(String title) throws HeadlessException {
        super(title);
        setVisible(true);
        setSize(1300, 900);
        setLocation(350, 50);
        setResizable(false);
        setLayout(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

}

class CpuThread extends Thread {
    static int flag = 0;
    public static Queue<Integer> queue = new LinkedList<Integer>();
    cenPanel cenpanel;

    public CpuThread(cenPanel cenpanel) {
        this.cenpanel = cenpanel;
    }

    public void run() {
        if (queue.peek() == null) {
            System.out.println("不管");
            cenpanel.cenlabel.setText("所有进程已结束");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else if (queue.peek() == 1) {
            int x1 = cenpanel.label[0].getX();
            x1 = x1 + 3;
            cenpanel.label[0].setLocation(x1, 165);
            cenPanel.g1 = cenPanel.g1 + 3;
            cenpanel.repaint();
            cenpanel.cenlabel.setText("进程1正在使用CPU");
            cenpanel.cenlabel.setVisible(true);
            if (cenPanel.g1 > 1150) {
                queue.poll();
            }
        } else if (queue.peek() == 2) {
            int x2 = cenpanel.label[1].getX();
            x2 = x2 + 3;
            cenpanel.label[1].setLocation(x2, 315);
            cenPanel.g2 = cenPanel.g2 + 3;
            cenpanel.repaint();
            cenpanel.cenlabel.setText("进程2正在使用CPU");
            cenpanel.cenlabel.setVisible(true);
            if (cenPanel.g2 > 1150) {
                queue.poll();
            }
        } else if (queue.peek() == 3) {
            int x3 = cenpanel.label[2].getX();
            x3 = x3 + 3;
            cenpanel.label[2].setLocation(x3, 465);
            cenPanel.g3 = cenPanel.g3 + 3;
            cenpanel.repaint();
            cenpanel.cenlabel.setText("进程3正在使用CPU");
            cenpanel.cenlabel.setVisible(true);
            if (cenPanel.g3 > 1150) {
                queue.poll();
            }
        } else if (queue.peek() == 4) {
            int x4 = cenpanel.label[3].getX();
            x4 = x4 + 3;
            cenpanel.label[3].setLocation(x4, 615);
            cenPanel.g4 = cenPanel.g4 + 3;
            cenpanel.repaint();
            cenpanel.cenlabel.setText("进程4正在使用CPU");
            cenpanel.cenlabel.setVisible(true);
            if (cenPanel.g4 > 1150) {
                queue.poll();
            }
        }
    }
}