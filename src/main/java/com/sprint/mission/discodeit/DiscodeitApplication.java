package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService us = context.getBean(UserService.class);
        ChannelService cs = context.getBean(ChannelService.class);
        MessageService ms = context.getBean(MessageService.class);

        User currentUser = initData(us, cs, ms);

        testPrintAllEntities(us, cs, ms, currentUser);
        testModifyEntities(us, cs, ms, currentUser);
        testDeleteEntities(us, cs, ms, currentUser);
    }


    public static User initData(UserService us, ChannelService cs, MessageService ms) {
        User user1 = saveMethod(us, new User("test1", "test1@aaa.com", "1234"));
        User user2 = saveMethod(us, new User("test2", "test2@aaa.com", "2345"));
        User user3 = saveMethod(us, new User("test3", "test3@aaa.com", "3412"));

        Channel[] ch = new Channel[3];
        ch[0] = saveMethod(cs, new Channel(user1, "test1_channel"));
        ch[1] = saveMethod(cs, new Channel(user2, "test2_channel"));
        ch[2] = saveMethod(cs, new Channel(user3, "test3_channel"));
        if (ms.findAll().isEmpty()) {
            for (int i = 0; i < 3; i++) {
                saveMethod(ms, new Message(ch[i].getId(), user1.getId(), "test" + (i + 11)));
                saveMethod(ms, new Message(ch[i].getId(), user2.getId(), "test" + (i + 21)));
                saveMethod(ms, new Message(ch[i].getId(), user3.getId(), "test" + (i + 31)));
            }
        }
//        size == 8로 검증하는 이유는 deleteMethod를 한 후 재실행시 오류 방지.
        if (ms.findAll().size() == 8) {
            saveMethod(ms, new Message(ch[0].getId(), user3.getId(), "test444"));
        }
        return user3;
    }

    public static void testPrintAllEntities(UserService us, ChannelService cs, MessageService ms, User currentUser) {
        System.out.println("\n==========조회==========");
        System.out.println("++++++++++전체++++++++++");
        System.out.println("----------유저 조회----------");
        us.findAll().forEach(System.out::println);

        System.out.println("----------채널 조회----------");
        cs.findAll().forEach(System.out::println);

        System.out.println("----------메시지 조회----------");
        ms.findAll().forEach(System.out::println);

        System.out.println("++++++++++단건++++++++++");
        System.out.println("----------유저 조회----------");
        System.out.println(us.findByUsername(currentUser.getUsername()));

        System.out.println("----------채널 조회----------");
        System.out.println(cs.findByName("test3_channel"));

        System.out.println("----------메시지 조회----------");
        System.out.println(ms.findById(ms.findByUserId(currentUser.getId()).get(0).getId()));
    }

    public static void testModifyEntities(UserService us, ChannelService cs, MessageService ms, User currentUser) {
        System.out.println("\n==========수정==========");

        System.out.println("++++++++++유저 수정++++++++++");
        System.out.println("----------유저 수정(username)----------");
        updateMethod(us, currentUser, currentUser,
                new User("test1", currentUser.getEmail(),
                        currentUser.getPassword())
        );
        updateMethod(us, currentUser, currentUser,
                new User("test_username_수정", currentUser.getEmail(),
                        currentUser.getPassword())
        );

        System.out.println("++++++++++채널 수정(name)++++++++++");
        Channel channel1 = cs.findByOwner(us.findByUsername("test1").getId()).get(0);
        Channel channel3 = cs.findByOwner(currentUser.getId()).get(0);

        updateMethod(us, cs, currentUser, channel3, "test33333_channel");

        System.out.println("++++++++++메시지 수정(content)++++++++++");
        System.out.println("채널 1을 순회하며 currentUser로 메시지 수정.");

        for (Message message : ms.findByChannelId(channel1.getId())) {
            updateMethod(us, ms, currentUser, message, "test_메시지_변경");
        }
    }

    public static void testDeleteEntities(UserService us, ChannelService cs, MessageService ms, User currentUser) {
        Channel channel1 = cs.findByOwner(us.findByUsername("test1").getId()).get(0);
        System.out.println("\n==========삭제==========");
        System.out.println("++++++++++유저 삭제++++++++++");

        for (User user : us.findAll()) {
            deleteMethod(us, currentUser, user);
        }

        System.out.println("++++++++++채널 삭제++++++++++");
        for (Channel channel : cs.findAll()) {
            deleteMethod(us, cs, currentUser, channel);
        }

        System.out.println("++++++++++메시지 삭제++++++++++");
        for (Message message : ms.findByChannelId(channel1.getId())) {
            deleteMethod(us, ms, currentUser, message);
        }
    }

    public static User saveMethod(UserService us, User userData) {
        if (!us.isUniqueUsername(userData.getUsername())) {
            return us.findByUsername(userData.getUsername());
        }

        try {
            return us.save(userData);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static Channel saveMethod(ChannelService cs, Channel channelData) {
        if (!cs.isUniqueName(channelData.getName())) {
            return cs.findByName(channelData.getName());
        }

        try {
            return cs.save(channelData);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static Message saveMethod(MessageService ms, Message messageData) {
        try {
            return ms.save(messageData);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static void updateMethod(UserService us, User srcUser, User dstUser, User userData) {
        try {
            String temp = dstUser.toString();

            if (us.update(srcUser.getId(), dstUser.getId(), userData)) {
                System.out.println("수정 성공");
                System.out.println("전 : " + temp);
                System.out.println("후 : " + us.findById(srcUser.getId()));
                us.findAll().forEach(System.out::println);
            } else System.out.println("수정 실패\t권한이 있는 유저 : " + dstUser.getUsername());
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println();
        }
    }

    public static void updateMethod(UserService us, ChannelService cs, User srcUser, Channel channel, String name) {
        try {
            String temp = channel.toString();

            if (cs.update(srcUser.getId(), channel.getId(), name)) {
                System.out.println("수정 성공");
                System.out.println("전 : " + temp);
                System.out.println("후 : " + cs.findById(channel.getId()));
                cs.findAll().forEach(System.out::println);
            } else System.out.println("수정 실패\t권한이 있는 유저 : " + us.findById(channel.getOwnerId()).getUsername());
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println();
        }
    }

    public static void updateMethod(UserService us, MessageService ms, User srcUser, Message message, String content) {
        try {
            String temp = message.toString();

            if (ms.update(srcUser.getId(), message.getId(), content)) {
                System.out.println("수정 성공");
                System.out.println("전 : " + temp);
                System.out.println("후 : " + ms.findById(message.getId()));
                ms.findByChannelId(message.getChannelId()).forEach(System.out::println);
            } else System.out.println("수정 실패\t권한이 있는 유저 : " + us.findById(message.getUserId()).getUsername());
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println();
        }
    }

    public static void deleteMethod(UserService us, User srcUser, User dstUser) {
        try {
            if (us.delete(srcUser.getId(), dstUser.getId())) {
                System.out.println("삭제 성공.");
                System.out.println("삭제 전 저장한 객체 : " + dstUser);
                try{
                    System.out.println("삭제 후 서비스로 id호출 : " + us.findById(dstUser.getId()));
                }
                catch (Exception e){
                    System.out.println("삭제 후 서비스로 id호출 : 조회 실패 (" + e.getMessage() + ")");
                }
                us.findAll().forEach(System.out::println);
            } else System.out.println("삭제 실패\t권한이 있는 유저 : " + dstUser.getUsername());
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println();
        }
    }

    public static void deleteMethod(UserService us, ChannelService cs, User srcUser, Channel channel) {
        try {
            if (cs.delete(srcUser.getId(), channel.getId())) {
                System.out.println("삭제 성공.");
                System.out.println("삭제 전 저장한 객체 : " + channel);
                try{
                    System.out.println("삭제 후 서비스로 id호출 : " + cs.findById(channel.getId()));
                }
                catch (Exception e){
                    System.out.println("삭제 후 서비스로 id호출 : 조회 실패 (" + e.getMessage() + ")");
                }
                cs.findAll().forEach(System.out::println);
            } else System.out.println("삭제 실패\t권한이 있는 유저 : " + us.findById(channel.getOwnerId()).getUsername());
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println();
        }
    }

    public static void deleteMethod(UserService us, MessageService ms, User srcUser, Message message) {
        try {
            if (ms.delete(srcUser.getId(), message.getId())) {
                System.out.println("삭제 성공.");
                System.out.println("삭제 전 저장한 객체 : " + message);
                try{
                    System.out.println("삭제 후 서비스로 id호출 : " + ms.findById(message.getId()));
                }
                catch (Exception e){
                    System.out.println("삭제 후 서비스로 id호출 : 조회 실패 (" + e.getMessage() + ")");
                }
                ms.findByChannelId(message.getChannelId()).forEach(System.out::println);
            } else System.out.println("삭제 실패\t권한이 있는 유저 : " + us.findById(message.getUserId()).getUsername());
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println();
        }
    }
}
