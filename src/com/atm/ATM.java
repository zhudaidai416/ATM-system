package com.atm;

import java.util.ArrayList;
import java.util.Scanner;

public class ATM {
  private ArrayList<Account> accounts = new ArrayList<>();
  private Scanner sc = new Scanner(System.in);

  /**
   * 启动系统，展示欢迎界面
   */
  public void start() {
    while (true) {
      System.out.println("=== 欢迎进入ATM系统 ===");
      System.out.println("1、用户登录");
      System.out.println("2、用户开户");
      System.out.print("请选择：");
      int command = sc.nextInt();
      switch (command) {
        case 1:
          break;
        case 2:
          createAccount();
          break;
        default:
          System.out.println("输入错误~~请重新选择！");
      }
    }
  }

  /**
   * 用户开户
   */
  private void createAccount() {
    System.out.println("=== 系统开户操作 ===");
    Account acc = new Account();

    System.out.print("请输入你的账户名称：");
    String name = sc.next();
    acc.setUsername(name);

    while (true) {
      System.out.print("请输入你的性别：");
      char sex = sc.next().charAt(0);
      if (sex == '男' || sex == '女') {
        acc.setSex(sex);
        break;
      } else {
        System.out.println("输入的性别有误~~");
      }
    }

    while (true) {
      System.out.print("请输入你的账户密码：");
      String password = sc.next();
      System.out.print("请输入你的确认密码：");
      String okPassword = sc.next();
      if (okPassword.equals(password)) {
        acc.setPassword(password);
        break;
      } else {
        System.out.println("输入的两次密码不一致~~");
      }
    }

    System.out.print("请输入你的取现额度：");
    double limit = sc.nextDouble();
    acc.setLimit(limit);

    // 自动生成卡号：8位卡号，不能重复

    accounts.add(acc);
    System.out.println("恭喜你，" + acc.getUsername() + "开户成功，你的卡号是" + acc.getCardId());
  }
}
