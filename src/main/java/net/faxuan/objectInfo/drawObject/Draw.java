package net.faxuan.objectInfo.drawObject;

import net.faxuan.choujiang.core.Http;
import net.faxuan.choujiang.core.Response;
import net.faxuan.choujiang.exception.CheckException;
import net.faxuan.choujiang.util.ExcelUtil;
import net.faxuan.choujiang.util.GetProperties;
import net.faxuan.choujiang.util.JsonHelper;
import net.faxuan.objectInfo.userObject.User;
import net.faxuan.objectInfo.userObject.UserPool;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static net.faxuan.choujiang.core.Http.get;

/**
 * Created with IntelliJ IDEA.
 * FileName: Draw.java
 * Author: song
 * Date: 2019/6/13 14:54
 * To change this template use File | Settings | File and Code Templates | Includes | File Header
 * Description:
 */
public class Draw extends JFrame {

    /**
     * log4j打log
     */
    private static Logger log = Logger.getLogger(Draw.class);

    JFrame jf = new JFrame("法治宝典积分抽奖测试");

    JTextField activateId = new JTextField(20);
    JTextField drawPeople = new JTextField(20);
    JTextField drawNumber = new JTextField(20);
    JTextField loopNumber = new JTextField(15);
    JButton inputTest = new JButton("测试输入数据");
    private static JLabel inputTestInfo = new JLabel();

    JButton chooseButton = new JButton("选择Excel输出");
    JTextField excelLocation = new JTextField(20);

    JButton start = new JButton("开始测试");
    JLabel testInfo = new JLabel();

    /**
     * 默认信息
     */
    String id = "3";
    String pNumber = "100";
    String dNumber = "3";
    String lNumber = "4";
    String excel = "D:\\测试结果.xlsx";
    public void CreatJFrame() {

        GridLayout gy = new GridLayout(11, 0);
        jf.setSize(600, 400);
        jf.setLocation(500, 200);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLayout(gy);
        jf.setResizable(false);

        ImageIcon icon = new ImageIcon(this.getClass().getResource("/icon.png")); //图片和项目同一路径，故不用图片的路径
//        ImageIcon icon = new ImageIcon(this.getClass().getClassLoader().getResource("icon.png").getPath());//测试运行
        jf.setIconImage(icon.getImage());

        inputTest.addActionListener(new InputTestHandler());
        chooseButton.addActionListener(new ChooseButtonListener());
        start.addActionListener(new StartButtonListener());
        JLabel jl_firstline = new JLabel("=========================测试传参=========================");
        JLabel jl_activateId = new JLabel("活动ID:");
        JLabel jl_drawPeople = new JLabel("抽奖人数:");
        JLabel jl_drawNumber = new JLabel("抽奖次数:");
        JLabel jl_loopNumber = new JLabel("循环次数:");
        JLabel jl_towline = new JLabel("======================输出excel配置=====================");


        JPanel firstLine = new JPanel();
        firstLine.add(jl_firstline);

        JPanel jPanel1 = new JPanel();
        jPanel1.add(jl_activateId);
        jPanel1.add(activateId);
        activateId.setForeground(Color.GRAY);
        activateId.setText(id);
        activateId.addFocusListener(new MyFocusListener(id, activateId));

        JPanel jPanel2 = new JPanel();
        jPanel2.add(jl_drawPeople);
        jPanel2.add(drawPeople);
        drawPeople.setForeground(Color.GRAY);
        drawPeople.setText(pNumber);
        drawPeople.addFocusListener(new MyFocusListener(pNumber, drawPeople));

        JPanel jPanel3 = new JPanel();
        jPanel3.add(jl_drawNumber);
        jPanel3.add(drawNumber);
        drawNumber.setForeground(Color.GRAY);
        drawNumber.setText(dNumber);
        drawNumber.addFocusListener(new MyFocusListener(dNumber, drawNumber));

        JPanel jPanel4 = new JPanel();
        jPanel4.add(jl_loopNumber);
        jPanel4.add(loopNumber);
        loopNumber.setForeground(Color.GRAY);
        loopNumber.setText(lNumber);
        loopNumber.addFocusListener(new MyFocusListener(lNumber, loopNumber));

        JPanel inputTestButton = new JPanel();
        inputTestButton.add(inputTest);

        JPanel JL_inputTestInfo = new JPanel();
        JL_inputTestInfo.add(inputTestInfo);

        JPanel insLine = new JPanel();
        insLine.add(jl_towline);

        JPanel jPanelexcel = new JPanel();
        jPanelexcel.add(chooseButton);
        jPanelexcel.add(excelLocation);
        excelLocation.setEditable(false);
        jPanelexcel.add(excelLocation);
        excelLocation.setForeground(Color.GRAY);
        excelLocation.setText(excel);
        excelLocation.addFocusListener(new MyFocusListener(excel, excelLocation));


        JPanel jpconvert = new JPanel();
        jpconvert.add(start);

        JPanel convertInfo1 = new JPanel();
        convertInfo1.add(testInfo);

        jf.add(firstLine);
        jf.add(jPanel1);
        jf.add(jPanel2);
        jf.add(jPanel3);
        jf.add(jPanel4);
        jf.add(inputTestButton);
        jf.add(JL_inputTestInfo);
        jf.add(insLine);
        jf.add(jPanelexcel);
        jf.add(jpconvert);
        jf.add(convertInfo1);

        jf.setVisible(true);
    }

    public static void main(String[] args) {
        new Draw().CreatJFrame();
    }

    /**
     * 测试按钮
     */
    class StartButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //导出Excel本地路径
            String outExcelFile = excelLocation.getText().trim();

            //用户池集合
            List<UserPool> userPools = new ArrayList<>();
            //用户抽奖返回集合
            List<RequestDraw> requestDraws = new ArrayList<>();

            /**
             * 根据循环次数生成指定数量的用户池集合
             */
            for (int i=0;i<Integer.valueOf(loopNumber.getText().trim());i++) {
                /**
                 * 生成用户池
                 */
                UserPool createUserPool = new UserPool(Integer.valueOf(drawPeople.getText().trim()),Integer.valueOf(drawNumber.getText().trim()));
                userPools.add(createUserPool);
            }

            /**
             * 循环用户池集合
             */
            for (int i=0;i<userPools.size();i++) {
                UserPool userPool=userPools.get(i);
                List<User> users = userPool.getUsers();
                //清理结果集
                requestDraws.clear();
                //创建线程池
                ExecutorService executorService = Executors.newFixedThreadPool(1000);
                ExecutorCompletionService<RequestDraw> completionService = new ExecutorCompletionService<RequestDraw>(executorService);
                //循环用户池中所有用户
                int userCount = users.size();
                for (int a=0;a<userCount;a++) {
                    //随机拿取一个用户
                    User user = userPool.getOneUser(users);

                    completionService.submit(new Callable<RequestDraw>() {
                        @Override
                        public RequestDraw call() throws Exception {
                            return new RequestDraw().doRequest(Integer.valueOf(activateId.getText().trim()),user.getUserAccount(),user.getSid());
//                            return null;
                        }
                    });
                }
                try {
                    int completionTask = 0;
                    while(completionTask < userCount) {
                        Future<RequestDraw> resultHolder = completionService.take();
                        requestDraws.add(resultHolder.get());
                        completionTask ++;
                    }
                    log.info("第" + (i+1) + "轮的" + completionTask + "个任务执行完毕 !");
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                } catch (ExecutionException e1) {
                    e1.printStackTrace();
                }

                //第一个用户池中所有用户都抽奖完毕后关闭线程池
                executorService.shutdown();
                //检查线程池中所有线程是否都执行完毕
                while (! executorService.isTerminated()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException itde) {
                        itde.printStackTrace();
                    }
                }

                /**
                 * 检查输出Excel是否存在
                 * 不存在则创建
                 */
                if (i==0 && new File(outExcelFile).exists()) {
                    new File(outExcelFile).delete();
                    //循环次数
                    int loopTime = Integer.valueOf(loopNumber.getText().trim());
                    //所有sheet名称
                    List<String> sheets = new ArrayList<>();
                    for (int j = 1;j<=loopTime;j++) {
                        sheets.add("第" + j + "轮抽奖");
                    }
                    //每个sheet中需要添加的列名
                    List<String> columns = new ArrayList<>();
                    columns.add("用户账号");
                    columns.add("奖品名称");
//                    columns.add("抽奖时间");
                    try{
                        //创建输出excel文件
                        ExcelUtil.createExcel(outExcelFile, sheets, columns);
                    }catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else if (i==0 && ! new File(outExcelFile).exists()) {
                    //循环次数
                    int loopTime = Integer.valueOf(loopNumber.getText().trim());
                    //所有sheet名称
                    List<String> sheets = new ArrayList<>();
                    for (int j = 1;j<=loopTime;j++) {
                        sheets.add("第" + j + "轮抽奖");
                    }
                    //每个sheet中需要添加的列名
                    List<String> columns = new ArrayList<>();
                    columns.add("用户账号");
                    columns.add("奖品名称");
//                    columns.add("抽奖时间");
                    try{
                        //创建输出excel文件
                        ExcelUtil.createExcel(outExcelFile, sheets, columns);
                    }catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }

                //插入本轮抽奖结果到Excel中
                List<String[]> datas = new ArrayList<>();
                for (RequestDraw requestDraw:requestDraws) {
                    datas.add(new String[]{requestDraw.getUserAccount(),requestDraw.getPrizeName()});
                }
                int sum = i + 1;
                try {
                    ExcelUtil.writeToExcel(outExcelFile,"第" + sum + "轮抽奖",datas);
                } catch (Exception e1) {
                    log.error("测试结果写入到excel：" + outExcelFile + "文件失败");
                    e1.printStackTrace();
                }
                //一轮抽奖结束后等待3秒
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

            testInfo.setForeground(Color.BLUE);
            testInfo.setText("测试完成，测试结果文件为：" + outExcelFile);
            testInfo.repaint();
            log.info("所有轮的测试都已完成！！！");
        }
    }

    /**
     * 选择excel文件按钮监听
     */
    class ChooseButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser jfc=new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jfc.showDialog(new JLabel(), "选择");
            File file=jfc.getSelectedFile();
            if(file.isDirectory()){
                System.out.println("测试结果excel文件路径为:"+file.getAbsolutePath() + "\\测试结果.xlsx");
                excelLocation.setText(file.getAbsolutePath() + "\\测试结果.xlsx");
            }else {
                    excelLocation.setForeground(Color.MAGENTA);
                    excelLocation.setText("请选择excel导出文件夹。");
                }
//System.out.println("选择文件:"+file.getAbsolutePath());
        }
    }

    /**
     * 测试输入数据按钮
     */
    class InputTestHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            //事件处理器
            inputTestInfo.setForeground(Color.BLACK);
            inputTestInfo.setText("测试中请稍后。。。");
            jf.repaint();

            if (! IsActivateId(activateId.getText().trim())) {
                inputTestInfo.setForeground(Color.red);
                inputTestInfo.setText("输入活动ID在系统中不存在");
                inputTestInfo.repaint();
                return;
            }
            int drawPeopleN = Integer.valueOf(drawPeople.getText().trim());
            if (drawPeopleN < 1 || drawPeopleN > 1000) {
                inputTestInfo.setForeground(Color.red);
                inputTestInfo.setText("抽奖人数必须是1-1000的整数");
                inputTestInfo.repaint();
                return;
            }

            int drawNumberN = Integer.valueOf(drawNumber.getText().trim());
            if (drawNumberN < 1 || drawNumberN > 10000) {
                inputTestInfo.setForeground(Color.red);
                inputTestInfo.setText("抽奖次数必须是1-10000的整数");
                inputTestInfo.repaint();
                return;
            }

            int loopNumberN = Integer.valueOf(loopNumber.getText().trim());
            if (loopNumberN < 1 || loopNumberN > 10) {
                inputTestInfo.setForeground(Color.red);
                inputTestInfo.setText("循环次数必须是1-10的整数");
                inputTestInfo.repaint();
                return;
            }

            inputTestInfo.setText("输入数据全部测试通过");
            inputTestInfo.setForeground(Color.blue);
            inputTestInfo.repaint();
        }
    }

    /**
     * 输入框默认文字监听
     */
    class MyFocusListener implements FocusListener {
        String info;
        JTextField jtf;

        public MyFocusListener(String info, JTextField jtf) {
            this.info = info;
            this.jtf = jtf;
        }

        public void focusGained(FocusEvent e) {//获得焦点的时候,转换文字为黑色
            String temp = jtf.getText();
            if (temp.equals(info)) {
                jtf.setText(jtf.getText().trim());
                jtf.setForeground(Color.BLACK);
            }
        }

        public void focusLost(FocusEvent e) {//失去焦点的时候,判断如果为空,就显示提示文字
            String temp = jtf.getText();
            if (temp.equals("")) {
                jtf.setForeground(Color.GRAY);
                jtf.setText(info);
            }
        }
    }

    /**
     * 检查输入活动ID是否存在
     * @param id
     * @return
     */
    private static boolean IsActivateId(String id) {
        //配置文件接口信息
        GetProperties getProperties = new GetProperties().getProperties();

        String drawInterface= getProperties.getGetActivateUrl();
        String[] dl = drawInterface.split("\\?");
        String url = dl[0];
        String[] paramsName = dl[1].split("&");

        Map<Object,Object> params = new HashMap<Object,Object>();
        params.put(paramsName[0],id);
        params.put(paramsName[1],"201906130026");
        params.put(paramsName[2],"ceshiUserDefultSid");
        String code = JsonHelper.getValue(Http.post(url,params).getBody(),"code").toString();
        if (code.equals("200")) {
            return true;
        }
        return false;
    }
}
