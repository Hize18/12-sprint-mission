package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {
//    의존성을 static으로 빼서 주입이 아니라 main에서 정의 및 주입으로
//    대신 파라미터로 매번 넘겨야 함.

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService us = context.getBean(UserService.class);
        ChannelService cs = context.getBean(ChannelService.class);
        MessageService ms = context.getBean(MessageService.class);
        AuthService auth = context.getBean(AuthService.class);
        BinaryContentService bc = context.getBean(BinaryContentService.class);
        UserStatusService uss = context.getBean(UserStatusService.class);
        ReadStatusService rss = context.getBean(ReadStatusService.class);

        UserResponse userResponse = initData(us, cs, ms);

        UserResponse currentUser = auth.login(new LoginRequest(userResponse.username(), "3456"));

        testPrintAllEntities(us, cs, ms, currentUser);
        testModifyEntities(us, cs, ms, currentUser);
        testPrivateChannel(us, cs, ms, bc, rss, currentUser);
        testUserStatus(us, cs, ms, uss,  currentUser);
        testDeleteEntities(us, cs, ms, currentUser);

//        clearData(Path.of(System.getProperty("user.dir"), "data_repo").toFile());
    }

    public static void clearData(File dir){
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    clearData(file); // 재귀
                } else {
                    file.delete();
                }
            }
        }
        dir.delete(); // 마지막에 디렉토리 삭제
    }

    public static UserResponse initData(UserService us, ChannelService cs, MessageService ms) {
        UserResponse user1 = saveMethod(us, new UserCreateRequest("test1", "test1@aaa.com", "1234", null));
        UserResponse user2 = saveMethod(us, new UserCreateRequest("test2", "test2@aaa.com", "2345", null));
        UserResponse user3 = saveMethod(us, new UserCreateRequest("test3", "test3@aaa.com", "3456", null));

        ChannelResponse ch1 = saveMethod(cs, new PublicChannelCreateRequest(user1.id(), "test1_channel"));
        ChannelResponse ch2 = saveMethod(cs, new PublicChannelCreateRequest(user2.id(), "test2_channel"));
        ChannelResponse ch3 = saveMethod(cs, new PublicChannelCreateRequest(user3.id(), "test3_channel"));

        if (ms.findByUserId(user1.id()).isEmpty()) {
            saveMethod(ms, new MessageCreateRequest(ch1.id(), user1.id(), "test_msg_ch1_1", List.of()));
            saveMethod(ms, new MessageCreateRequest(ch2.id(), user1.id(), "test_msg_ch2_1", List.of()));
        }

        if (ms.findByUserId(user2.id()).isEmpty()) {
            saveMethod(ms, new MessageCreateRequest(ch1.id(), user2.id(), "test_msg_ch1_2", List.of()));
            saveMethod(ms, new MessageCreateRequest(ch2.id(), user2.id(), "test_msg_ch2_2", List.of()));
        }
        saveMethod(ms, new MessageCreateRequest(ch3.id(), user3.id(), "test_msg_ch3_1", List.of()));

        return user3;
    }

    public static void testPrintAllEntities(UserService us, ChannelService cs, MessageService ms, UserResponse currentUser) {
        System.out.println("\n==========조회==========");
        System.out.println("----------유저 조회----------");
        us.findAll().forEach(System.out::println);

        System.out.println("----------채널 조회----------");
        cs.findAll().forEach(System.out::println);

        System.out.println("----------메시지 조회----------");
        ms.findAll().forEach(System.out::println);

        System.out.println("++++++++++단건++++++++++");
        System.out.println("----------유저 조회----------");
        System.out.println(us.findByUsername(currentUser.username()));

        System.out.println("----------채널 조회----------");
        System.out.println(cs.findByName("test3_channel"));

        System.out.println("----------메시지 조회----------");
        System.out.println(ms.findById(ms.findByUserId(currentUser.id()).get(0).id()));
    }

    public static void testModifyEntities(UserService us, ChannelService cs, MessageService ms, UserResponse currentUser) {
        System.out.println("\n==========수정==========");

        System.out.println("++++++++++유저 수정++++++++++");
        System.out.println("----------유저 수정(username)----------");
        updateMethod(us, currentUser.id(), currentUser.id(),
                new UserUpdateRequest("test2", currentUser.email(),
                       null , null)
        );
        updateMethod(us, currentUser.id(), currentUser.id(),
                new UserUpdateRequest("test_username_수정", currentUser.email(),
                        null,  null)
        );

        System.out.println("++++++++++채널 수정(name)++++++++++");
        ChannelResponse channel3 = cs.findAll().stream()
                .filter(c -> c.ownerId().equals(currentUser.id()))
                .findFirst().get();
        ChannelResponse channel2 = cs.findByName("test2_channel");

        updateMethod(us, cs, currentUser.id(), new ChannelUpdateRequest(channel3.id(), "test_channel_33333"));
        updateMethod(us, cs, currentUser.id(), new ChannelUpdateRequest(channel2.id(), "test_channel_22222"));

        System.out.println("++++++++++메시지 수정(content)++++++++++");
        System.out.println("채널 1을 순회하며 currentUser로 메시지 수정.");

        for (MessageResponse message : ms.findAllByChannelId(channel2.id())) {
            updateMethod(us, ms, currentUser.id(), new MessageUpdateRequest(message.id(), "test_메시지_변경", List.of()));
        }
    }

    public static void testDeleteEntities(UserService us, ChannelService cs, MessageService ms, UserResponse currentUser) {
        ChannelResponse channel3 = cs.findAll().stream()
                .filter(c -> c.ownerId().equals(currentUser.id()))
                .findFirst().get();
        System.out.println("\n==========삭제==========");
        System.out.println("++++++++++메시지 삭제++++++++++");
        for (MessageResponse message : ms.findAllByChannelId(channel3.id())) {
            deleteMethod(us, ms, currentUser.id(), message);
        }

        System.out.println("++++++++++채널 삭제++++++++++");
        for (ChannelResponse channel : cs.findAll()) {
            deleteMethod(us, cs, currentUser.id(), channel);
        }

        System.out.println("++++++++++유저 삭제++++++++++");
        for (UserResponse user : us.findAll()) {
            deleteMethod(us, currentUser.id(), user);
        }
    }

    public static void testPrivateChannel(UserService us, ChannelService cs, MessageService ms, BinaryContentService bc, ReadStatusService rss, UserResponse currentUser) {
        System.out.println("----------PrivateChannel----------");
        ChannelResponse privateChannel = saveMethod(cs,
                new PrivateChannelCreateRequest(currentUser.id(), List.of(currentUser.id())));

        System.out.println("+++++전체 채널 조회+++++");
        cs.findAll().forEach(System.out::println);
        System.out.println("\n+++++UserId를 통한 채널 조회+++++");
        cs.findAllByUserId(currentUser.id()).forEach(System.out::println);

        System.out.println("\n+++++private channel 수정+++++");
        updateMethod(us, cs, currentUser.id(), new ChannelUpdateRequest(privateChannel.id(), "private_channnnel"));

        saveMethod(ms, new MessageCreateRequest(privateChannel.id(), currentUser.id(), "private_test",
                List.of(
                        new BinaryContentCreateRequest("test_bc_1", "text", "test_bc_1".getBytes())
                )));

        System.out.println("+++++private 채널 생성 후 메시지 등록 및 readStatus 조회+++++");
        ms.findAllByChannelId(privateChannel.id()).forEach(System.out::println);
        System.out.println();
        rss.findAllByUserId(currentUser.id()).forEach(System.out::println);

        System.out.println("\n+++++private 채널 삭제+++++");
        cs.delete(currentUser.id(), privateChannel.id());
    }

    public static void testUserStatus(UserService us, ChannelService cs, MessageService ms,
                                      UserStatusService uss, UserResponse currentUser) {
        uss.findAll().forEach(System.out::println);
    }

    public static UserResponse saveMethod(UserService us, UserCreateRequest request) {
        if (!us.isUniqueUsername(request.username())) {
            return us.findByUsername(request.username());
        }

        if (!us.isUniqueEmail(request.email())) {
            return us.findAll().stream().filter(u -> u.email().equals(request.email())).findFirst().get();
        }

        try {
            return us.create(request);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static ChannelResponse saveMethod(ChannelService cs, PublicChannelCreateRequest request) {
        if (!cs.isUniqueName(request.name())) {
            return cs.findByName(request.name());
        }

        try {
            return cs.createPublicChannel(request);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static ChannelResponse saveMethod(ChannelService cs, PrivateChannelCreateRequest request) {
        try {
            return cs.createPrivateChannel(request);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static MessageResponse saveMethod(MessageService ms, MessageCreateRequest request) {
        try {
            return ms.create(request);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static void updateMethod(UserService us, UUID srcUserId, UUID dstUserId, UserUpdateRequest userData) {
        try {
            String temp = us.findById(srcUserId).toString();

            if (us.update(srcUserId, dstUserId, userData)) {
                System.out.println("수정 성공");
                System.out.println("전 : " + temp);
                System.out.println("후 : " + us.findById(srcUserId));
                us.findAll().forEach(System.out::println);
            } else System.out.println("수정 실패\t권한이 있는 유저 : " + us.findById(dstUserId).username());
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println();
        }
    }

    public static void updateMethod(UserService us, ChannelService cs, UUID srcUserId, ChannelUpdateRequest request) {
        try {
            String temp = cs.findById(request.channelId()).toString();

            if (cs.update(srcUserId, request)) {
                System.out.println("수정 성공");
                System.out.println("전 : " + temp);
                System.out.println("후 : " + cs.findById(request.channelId()));
                cs.findAll().forEach(System.out::println);
            } else System.out.println("수정 실패\t권한이 있는 유저 : " + us.findById(cs.findById(request.channelId()).ownerId()).username());
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println();
        }
    }

    public static void updateMethod(UserService us, MessageService ms, UUID srcUserId, MessageUpdateRequest request) {
        try {
            String temp = ms.findById(request.messageId()).toString();

            if (ms.update(srcUserId, request)) {
                System.out.println("수정 성공");
                System.out.println("전 : " + temp);
                System.out.println("후 : " + ms.findById(request.messageId()));
                ms.findAllByChannelId(ms.findById(request.messageId()).channelId()).forEach(System.out::println);
            } else System.out.println("수정 실패\t권한이 있는 유저 : " + us.findById(ms.findById(request.messageId()).userId()).username());
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println();
        }
    }

    public static void deleteMethod(UserService us, UUID srcUserId, UserResponse dstUser) {
        try {
            if (us.delete(srcUserId, dstUser.id())) {
                System.out.println("삭제 성공.");
                System.out.println("삭제 전 저장한 객체 : " + dstUser);
                try{
                    System.out.println("삭제 후 서비스로 id호출 : " + us.findById(dstUser.id()));
                }
                catch (Exception e){
                    System.out.println("삭제 후 서비스로 id호출 : 조회 실패 (" + e.getMessage() + ")");
                }
                us.findAll().forEach(System.out::println);
            } else System.out.println("삭제 실패\t권한이 있는 유저 : " + dstUser.username());
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println();
        }
    }

    public static void deleteMethod(UserService us, ChannelService cs, UUID srcUserId, ChannelResponse channel) {
        try {
            if (cs.delete(srcUserId, channel.id())) {
                System.out.println("삭제 성공.");
                System.out.println("삭제 전 저장한 객체 : " + channel);
                try{
                    System.out.println("삭제 후 서비스로 id호출 : " + cs.findById(channel.id()));
                }
                catch (Exception e){
                    System.out.println("삭제 후 서비스로 id호출 : 조회 실패 (" + e.getMessage() + ")");
                }
                cs.findAll().forEach(System.out::println);
            } else System.out.println("삭제 실패\t권한이 있는 유저 : " + us.findById(channel.ownerId()).username());
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println();
        }
    }

    public static void deleteMethod(UserService us, MessageService ms, UUID srcUserId, MessageResponse message) {
        try {
            if (ms.delete(srcUserId, message.id())) {
                System.out.println("삭제 성공.");
                System.out.println("삭제 전 저장한 객체 : " + message);
                try{
                    System.out.println("삭제 후 서비스로 id호출 : " + ms.findById(message.id()));
                }
                catch (Exception e){
                    System.out.println("삭제 후 서비스로 id호출 : 조회 실패 (" + e.getMessage() + ")");
                }
                ms.findAllByChannelId(message.channelId()).forEach(System.out::println);
            } else System.out.println("삭제 실패\t권한이 있는 유저 : " + us.findById(message.userId()).username());
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println();
        }
    }
}
