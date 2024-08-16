package com.atm;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ATM {
  private ArrayList<Account> accounts = new ArrayList<>();
  private Scanner sc = new Scanner(System.in);
  private Account loginAcc;

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
          login();
          break;
        case 2:
          createAccount();
          break;
        default:
          System.out.println("输入错误，请重新选择~~");
      }
    }
  }

  /**
   * 1、用户开户
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
        System.out.println("输入的性别错误~~");
      }
    }

    while (true) {
      System.out.print("请输入你的账户密码：");
      String password = sc.next();
      System.out.print("请输入你的确认密码：");
      String okPassword = sc.next();
      if (okPassword.equals(password)) {
        acc.setPassword(okPassword);
        break;
      } else {
        System.out.println("两次密码不一致~~");
      }
    }

    System.out.print("请输入你的取现额度：");
    double limit = sc.nextDouble();
    acc.setLimit(limit);

    // 自动生成卡号：8位卡号，不能重复
    String cardId = createCardId();
    acc.setCardId(cardId);

    accounts.add(acc);
    System.out.println("恭喜你，" + acc.getUsername() + "开户成功！你的卡号是" + acc.getCardId());
  }

  /**
   * 自动生成卡号：8位卡号，不能重复
   */
  private String createCardId() {
    while (true) {
      String cardId = "";
      Random r = new Random();
      for (int i = 0; i < 8; i++) {
        int data = r.nextInt(10);
        cardId += data;
      }
      // 判断是否重复
      Account acc = getAccountByCardId(cardId);
      if (acc == null) {
        return cardId;
      }
    }
  }

  /**
   * 根据卡号查询，返回账号对象
   */
  private Account getAccountByCardId(String cardId) {
    for (int i = 0; i < accounts.size(); i++) {
      Account acc = accounts.get(i);
      if (acc.getCardId().equals(cardId)) {
        return acc;
      }
    }
    return null;
  }

  /**
   * 2、用户登录
   */
  private void login() {
    System.out.println("=== 系统登录操作 ===");
    if (accounts.size() == 0) {
      System.out.println("当前系统无任何账户，请进行开户！");
      return;
    }

    while (true) {
      System.out.print("请输入你的卡号：");
      String cardId = sc.next();
      Account acc = getAccountByCardId(cardId);
      if (acc == null) {
        System.out.println("卡号不存在，请重新输入~~");
      } else {
        while (true) {
          System.out.print("请输入你的密码：");
          String password = sc.next();
          if (acc.getPassword().equals(password)) {
            loginAcc = acc;
            System.out.println("欢迎，" + acc.getUsername() + "成功登录系统，你的卡号是" + acc.getCardId());
            showUserCommand();
            return;
          } else {
            System.out.println("密码错误，请重新登录~~");
          }
        }
      }
    }
  }

  /**
   * 用户操作界面
   */
  private void showUserCommand() {
    while (true) {
      System.out.println("--------------------------");
      System.out.println(loginAcc.getUsername() + "，你可以选择以下功能进行账户处理：");
      System.out.println("1、查询账户");
      System.out.println("2、存款");
      System.out.println("3、取款");
      System.out.println("4、转账");
      System.out.println("5、密码修改");
      System.out.println("6、退出");
      System.out.println("7、注销账户");
      System.out.println("--------------------------");
      System.out.print("请选择：");
      int command = sc.nextInt();
      switch (command) {
        case 1:
          showLoginAccount();
          break;
        case 2:
          depositMoney();
          break;
        case 3:
          drawMoney();
          break;
        case 4:
          transferMoney();
          break;
        case 5:
          updatePassword();
          return;
        case 6:
          System.out.println(loginAcc.getUsername() + "，你已成功退出系统！");
          return;
        case 7:
          if (deleteAccount()) {
            return;
          }
          break;
        default:
          System.out.println("输入错误，请重新选择~~");
      }
    }
  }

  /**
   * 查询账户
   */
  private void showLoginAccount() {
    System.out.println("=== 当前你的账户信息如下： ===");
    System.out.println("卡号：" + loginAcc.getCardId());
    System.out.println("户主：" + loginAcc.getUsername());
    System.out.println("性别：" + loginAcc.getSex());
    System.out.println("余额：" + loginAcc.getMoney());
    System.out.println("每次取款额度：" + loginAcc.getLimit());
  }

  /**
   * 存款
   */
  private void depositMoney() {
    System.out.println("=== 存款 ===");
    System.out.print("请输入存款金额：");
    double money = sc.nextDouble();
    loginAcc.setMoney(loginAcc.getMoney() + money);
    System.out.println("你已成功存款" + money + "元，当前账户余额：" + loginAcc.getMoney());
  }

  /**
   * 取款
   */
  private void drawMoney() {
    System.out.println("=== 取款 ===");
    if (loginAcc.getMoney() < 100) {
      System.out.println("你的账户余额不足100元，无法取钱~~");
      return;
    }

    while (true) {
      System.out.print("请输入取款金额：");
      double money = sc.nextDouble();
      if (loginAcc.getMoney() >= money) {
        if (money > loginAcc.getLimit()) {
          System.out.println("当前取款金额超过了每次限额，每次最多可取" + loginAcc.getLimit());
        } else {
          loginAcc.setMoney(loginAcc.getMoney() - money);
          System.out.println("你已成功取款" + money + "元，当前账户余额：" + loginAcc.getMoney());
          break;
        }
      } else {
        System.out.println("余额不足~~当前账户余额：" + loginAcc.getMoney());
      }
    }
  }

  /**
   * 转账
   */
  private void transferMoney() {
    System.out.println("=== 转账 ===");
    if (accounts.size() < 2) {
      System.out.println("当前系统只有一个账户，无法转账~~");
      return;
    }
    if (loginAcc.getMoney() == 0) {
      System.out.println("当前账户余额为0，无法转账~~");
      return;
    }

    while (true) {
      System.out.print("请输入对方卡号：");
      String cardId = sc.next();
      Account acc = getAccountByCardId(cardId);

      if (acc == null) {
        System.out.println("不存在该账户~~");
      } else if (loginAcc.getCardId().equals(acc.getCardId())) {
        System.out.println("该账户为自己的账号，无法转账~~");
      } else {
        String name = "*" + acc.getUsername().substring(1);
        System.out.print("请确认对方【" + name + "】的姓氏：");
        String preName = sc.next();
        if (acc.getUsername().startsWith(preName)) {
          while (true) {
            System.out.print("请输入转账金额：");
            double money = sc.nextDouble();
            if (loginAcc.getMoney() >= money) {
              loginAcc.setMoney(loginAcc.getMoney() - money);
              acc.setMoney(acc.getMoney() + money);
              System.out.println("你已成功转账" + money + "元，当前账户余额：" + loginAcc.getMoney());
              return;
            } else {
              System.out.println("余额不足，无法转账~~，当前账户余额：" + loginAcc.getMoney());
            }
          }
        } else {
          System.out.println("对不起，认证失败~~");
        }
      }
    }
  }

  /**
   * 密码修改
   */
  private void updatePassword() {
    System.out.println("=== 密码修改 ===");
    while (true) {
      System.out.print("请输入当前密码：");
      String password = sc.next();
      if (loginAcc.getPassword().equals(password)) {
        while (true) {
          System.out.print("请输入新密码：");
          String newPassword = sc.next();
          System.out.print("请输入确认密码：");
          String okPassword = sc.next();
          if (okPassword.equals(newPassword)) {
            loginAcc.setPassword(okPassword);
            System.out.println("你已成功修改密码，请重新登录！");
            return;
          } else {
            System.out.println("两次密码不一致~~");
          }
        }
      } else {
        System.out.println("密码错误~~");
      }
    }
  }

  /**
   * 注销账户
   */
  private boolean deleteAccount() {
    System.out.println("=== 注销账户 ===");
    System.out.print("确认要销户（y/n）？ 请选择：");
    String command = sc.next();
    switch (command) {
      case "y":
        if (loginAcc.getMoney() == 0) {
          accounts.remove(loginAcc);
          System.out.println("你已成功销户！");
          return true;
        } else {
          System.out.println("对不起，你的账户还有余额，不允许销户~~");
          return false;
        }
      default:
        System.out.println("好的，你的账户已保留！");
        return false;
    }
  }
}
