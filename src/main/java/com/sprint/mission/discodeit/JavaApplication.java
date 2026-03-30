package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;

public class JavaApplication {
    static User currentUser;


    public static void main(String[] args){
        UserService us = new FileUserService();
        ChannelService cs = new FileChannelService();
        MessageService ms = new FileMessageService();

        currentUser = initData(us, cs, ms);

        testPrintAllEntities(us, cs, ms);
//        testModifyEntities(us, cs, ms);
//        testDeleteEntities(us, cs, ms);

    }

    public static User initData(UserService us, ChannelService cs ,MessageService ms){
        User user1 = saveMethod(us, new User("test1","test1@aaa.com","1234","test1_nickname"));
        User user2 = saveMethod(us, new User("test2","test2@aaa.com","2345","test2_nickname"));
        User user3 = saveMethod(us, new User("test3", "test3@aaa.com", "3412", "test3_nickname"));

        Channel[] ch = new Channel[3];
        ch[0] = saveMethod(cs, new Channel(user1, "test1_channel", "test1_channel_handle"));
        ch[1] = saveMethod(cs, new Channel(user2, "test2_channel", "test2_channel_handle"));
        ch[2] = saveMethod(cs, new Channel(user3, "test3_channel", "test3_channel_handle"));
        if(ms.findAll().isEmpty()){
            for (int i = 0; i < 3; i++) {
                saveMethod(ms, new Message(ch[i], user1, "test"+(i+11)));
                saveMethod(ms, new Message(ch[i], user2, "test"+(i+21)));
                saveMethod(ms, new Message(ch[i], user3, "test"+(i+31)));
            }
        }
        if(ms.findAll().size()==8){
            saveMethod(ms, new Message(ch[0], user3, "test444"));
        }
        return user3;
    }

    public static void testPrintAllEntities(UserService us, ChannelService cs ,MessageService ms){
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
        cs.findByName("test3_channel").forEach(System.out::println);

        System.out.println("----------메시지 조회----------");
        System.out.println(ms.findById(ms.findByUserId(currentUser.getId()).get(0).getId()));
    }

    public static void testModifyEntities(UserService us, ChannelService cs ,MessageService ms){
        System.out.println("\n==========수정==========");

        System.out.println("++++++++++유저 수정++++++++++");
        System.out.println("----------유저 수정(nickname)----------");
        updateMethod(us, currentUser, currentUser,
                new User(currentUser.getUsername(), currentUser.getEmail(),
                        currentUser.getPassword(), "test1_nickname")
        );

        System.out.println("----------유저 수정(username)----------");
        updateMethod(us, currentUser, currentUser,
                    new User("test1", currentUser.getEmail(),
                            currentUser.getPassword(), currentUser.getNickname())
        );

        System.out.println("++++++++++채널 수정(name)++++++++++");
        Channel channel1 = cs.findByOwner(us.findByUsername("test1").getId()).get(0);
        Channel channel3 = cs.findByOwner(currentUser.getId()).get(0);

        updateMethod(cs, currentUser, channel3, "test33333_channel");

        System.out.println("++++++++++메시지 수정(content)++++++++++");
        System.out.println("채널 1을 순회하며 currentUser로 메시지 수정.");

        for (Message message : ms.findByChannelId(channel1.getId())) {
            updateMethod(ms, currentUser, message, "test_메시지_변경");
        }
    }

    public static void testDeleteEntities(UserService us, ChannelService cs ,MessageService ms){
        Channel channel1 = cs.findByOwner(us.findByUsername("test1").getId()).get(0);
        System.out.println("\n==========삭제==========");
        System.out.println("++++++++++유저 삭제++++++++++");

        for (User user : us.findAll()) {
            deleteMethod(us, currentUser, user);
        }

        System.out.println("++++++++++채널 삭제++++++++++");
        for (Channel channel : cs.findAll()) {
            deleteMethod(cs, currentUser, channel);
        }

        System.out.println("++++++++++메시지 삭제++++++++++");
        for (Message message : ms.findByChannelId(channel1.getId())) {
            deleteMethod(ms, currentUser, message);
        }
    }

    public static User saveMethod(UserService us, User userData){
        if(!us.isUniqueUsername(userData.getUsername())){
            return us.findByUsername(userData.getUsername());
        }

        try {
            return us.save(userData);
        } catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public static Channel saveMethod(ChannelService cs, Channel channelData){
        if(!cs.isUniqueHandle(channelData.getHandle())){
            return cs.findByHandle(channelData.getHandle());
        }

        try {
            return cs.save(channelData);
        }catch (Exception e){
            System.out.println(e);
            return  null;
        }
    }

    public static Message saveMethod(MessageService ms, Message messageData){
        try {
            return ms.save(messageData);
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public static void updateMethod(UserService us, User srcUser, User dstUser, User userData){
        try {
            String temp = dstUser.toString();

            if(us.update(srcUser.getId(), dstUser.getId(), userData)){
                System.out.println("수정 성공");
                System.out.println("전 : " + temp);
                System.out.println("후 : " + us.findById(srcUser.getId()));
                us.findAll().forEach(System.out::println);
            }else System.out.println("수정 실패\t권한이 있는 유저 : " + dstUser.getUsername());
        }catch (Exception e){
            System.out.println(e);
        }finally {
            System.out.println();
        }
    }

    public static void updateMethod(ChannelService cs, User srcUser, Channel channel, String name){
        try {
            String temp = channel.toString();

            if(cs.update(srcUser.getId(), channel.getId(), name)){
                System.out.println("수정 성공");
                System.out.println("전 : " + temp);
                System.out.println("후 : " + cs.findById(channel.getId()));
                cs.findAll().forEach(System.out::println);
            }else System.out.println("수정 실패\t권한이 있는 유저 : " + channel.getOwner().getUsername());
        }catch (Exception e){
            System.out.println(e);
        }finally {
            System.out.println();
        }
    }

    public static void updateMethod(MessageService ms, User srcUser, Message message, String content){
        try {
            String temp = message.toString();

            if(ms.update(srcUser.getId(), message.getId(), content)){
                System.out.println("수정 성공");
                System.out.println("전 : " + temp);
                System.out.println("후 : " + ms.findById(message.getId()));
                ms.findByChannelId(message.getChannel().getId()).forEach(System.out::println);
            }else System.out.println("수정 실패\t권한이 있는 유저 : " + message.getUser().getUsername());
        }catch (Exception e){
            System.out.println(e);
        }finally {
            System.out.println();
        }
    }

    public static void deleteMethod(UserService us, User srcUser, User dstUser){
        try {
            if(us.delete(srcUser.getId(), dstUser.getId())){
                System.out.println("삭제 성공.");
                System.out.println("삭제 전 저장한 객체 : " + dstUser);
                System.out.println("삭제 후 서비스로 id호출 : " + us.findById(dstUser.getId()));
                us.findAll().forEach(System.out::println);
            }else System.out.println("삭제 실패\t권한이 있는 유저 : " + dstUser.getUsername());
        }catch (Exception e){
            System.out.println(e);
        }finally {
            System.out.println();
        }
    }

    public static void deleteMethod(ChannelService cs, User srcUser, Channel channel){
        try {
            if(cs.delete(srcUser.getId(), channel.getId())){
                System.out.println("삭제 성공.");
                System.out.println("삭제 전 저장한 객체 : " + channel);
                System.out.println("삭제 후 서비스로 id호출 : " + cs.findById(channel.getId()));
                cs.findAll().forEach(System.out::println);
            }else System.out.println("삭제 실패\t권한이 있는 유저 : " + channel.getOwner().getUsername());
        }catch (Exception e){
            System.out.println(e);
        }finally {
            System.out.println();
        }
    }

    public static void deleteMethod(MessageService ms, User srcUser, Message message){
        try {
            if(ms.delete(srcUser.getId(), message.getId())){
                System.out.println("삭제 성공.");
                System.out.println("삭제 전 저장한 객체 : " + message);
                System.out.println("삭제 후 서비스로 id호출 : " + ms.findById(message.getId()));
                ms.findByChannelId(message.getChannel().getId()).forEach(System.out::println);
            }else System.out.println("삭제 실패\t권한이 있는 유저 : " + message.getUser().getUsername());
        }catch (Exception e){
            System.out.println(e);
        }finally {
            System.out.println();
        }
    }

}