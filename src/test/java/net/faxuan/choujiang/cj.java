package net.faxuan.choujiang;

import net.faxuan.choujiang.util.ExcelUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class cj {

public void aaa() {
    int loopTime = 5;
    List<String> sheets = new ArrayList<>();
    for (int i = 1;i<=loopTime;i++) {
        sheets.add("第" + i + "轮抽奖");
    }
    sheets.add("汇总结果");
    List<String> columns = new ArrayList<>();
    columns.add("用户账号");
    columns.add("奖品名称");
    columns.add("抽奖时间");
    try {
        ExcelUtil.createExcel("D:\\soroke.xlsx",sheets,columns);
    } catch (Exception e) {
        e.printStackTrace();
    }

    String[] sorokeaa={"user1","一等奖","20190606 15:34:23"};
    String[] sorokebb={"user2","二等奖","20190606 15:34:24"};
    String[] sorokecc={"user3","三等奖","20190606 15:34:25"};
    List<String[]> data = new ArrayList<>();
    data.add(sorokeaa);
    data.add(sorokebb);
    data.add(sorokecc);

    try {
        ExcelUtil.writeToExcel("D:\\soroke.xlsx","第一轮抽奖",data);
        data.clear();
        String[] s1={"s1","ONE","20190606 14:44:23"};
        String[] s2={"s2","TOW","20190606 14:44:24"};
        String[] s3={"s3","THREE","20190606 14:44:25"};
        data.add(s1);
        data.add(s2);
        data.add(s3);
        ExcelUtil.writeToExcel("D:\\soroke.xlsx","第二轮抽奖",data);
        ExcelUtil.writeToExcel("D:\\soroke.xlsx","第三轮抽奖",data);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public void test() {
        System.out.println(this.getClass().getClassLoader().getResource("ex.png").getPath());
    }




    public void asd() {
        String[] a = {"a1",""};
        String[] b = {"a2","阿萨德"};
        String[] c = {"a3",""};
        String[] d = {"a4","对对对"};
        String[] e = {"a5","啊啊啊"};
        String[] f = {"a6",""};
        List<String[]> data = new ArrayList<>();
        data.add(a);
        data.add(b);
        data.add(c);
        data.add(d);
        data.add(e);
        data.add(f);
    }
}
