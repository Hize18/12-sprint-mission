package com.sprint.mission.discodeit.test;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.Optional;
import java.util.Scanner;

public class TestUser {
    public static void main(String[] args) {
//        System.in 생략.
        UserService userService = new JCFUserService();

        User[] user = new User[3];
        for (int i = 0; i < 3; i++) {
            user[i] = new User(("test" + i), ("email" + i + "@test.co"),String.valueOf(i).repeat(4), ("nickname" + i));
            userService.save(user[i]);
        }

        System.out.println("---------------사용자 테스트 시작---------------");
        System.out.println("\n===============사용자 단일 검색===============");
        User tempUser = userService.findByUsername("test");
        System.out.println(tempUser);
        tempUser = userService.findByUsername("test0");
        System.out.println(tempUser);


        System.out.println("\n===============사용자 모두 검색===============");
        userService.findAll().forEach(System.out::println);

        System.out.println("\n===============사용자 정보 수정===============");
        User updateUser = userService.findByUsername("test0");

//        필드별 update 메소드 추가가 가장 좋지 않나?
        String username = updateUser.getUsername();
        String email = updateUser.getEmail();
        String password = updateUser.getPassword();
        String nickname = updateUser.getNickname();

        System.out.print("전 : ");
        System.out.println(updateUser);

        boolean flag = true;
        Scanner sc = new Scanner(System.in);

        while(flag){
            boolean inputFlag = true;
            while(inputFlag)
            {
                System.out.println("+++++현재+++++");
                System.out.println("username : " + username);
                System.out.println("email : " + email);
                System.out.println("password : " + password);
                System.out.println("nickname : " + nickname);
                System.out.println("++++++++++++++");

                System.out.println("1. username 변경");
                System.out.println("2. email 변경");
                System.out.println("3. password 변경");
                System.out.println("4. nickname 변경");
                System.out.println("0. 종료");

                int choice = Integer.parseInt(sc.nextLine());
                String inputStr = "testStr";

                switch (choice){
                    case 1 -> username = inputStr;
                    case 2 -> email = inputStr;
                    case 3 -> password = inputStr;
                    case 4 -> nickname = inputStr;
                    case 0 -> inputFlag = false;
                }
            }

            if (userService.update(updateUser.getId(), updateUser.getId(), new User(username, email, password, nickname))){
                System.out.println("성공");
                flag = false;
            }else System.out.println("실패");
        }

        System.out.print("후 : ");
        System.out.println(updateUser);

        System.out.println("\n===============사용자 삭제===============");
        userService.delete(updateUser.getId(), updateUser.getId());
        userService.findAll().forEach(System.out::println);

        System.out.println("---------------사용자 테스트 끝!!!---------------------\n");
    }
}
