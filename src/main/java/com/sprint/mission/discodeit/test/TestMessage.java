package com.sprint.mission.discodeit.test;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class TestMessage {
    public static void main(String[] args) {
//        System.in 생략
        MessageService messageService = new JCFMessageService();
        UserService userService = new JCFUserService();
        for (int i = 0; i < 3; i++) {
            userService.save(new User(("test" + i), ("email" + i + "@test.co"),String.valueOf(i).repeat(4), ("nickname" + i)));
        }
        User currentUser = userService.findAll().get(0);
//        채널 부분 추가
        ChannelService channelService = new JCFChannelService();
        String channelName = "메시지 테스트";
        channelService.save(new Channel(currentUser, channelName, channelService.createHandle(channelName)));


        System.out.println("---------------메세지 테스트---------------");
        System.out.println("===============메시지 입력부===============");
//        user가 이미 존재하고 특정 user로 메시지 등록한다고 가정
        for (int i = 0; i < 3; i++) {
            messageService.save(new Message(channelService.findAll().get(0),userService.findAll().get(i),
                    "test content"+i));
        }

        System.out.println("\n===============메시지 검색(사용자)===============");
//        메시지 검색은 두가지가 있음. 사용자 검색은 사용자 id로 사용자가 등록한 메시지등을 출력
        List<Message> tempMessage = messageService.findByUserId(currentUser.getId());
        tempMessage.forEach(System.out::println);

        tempMessage = messageService.findByUserId(UUID.randomUUID());
        tempMessage.forEach(System.out::println);

        System.out.println("\n===============메시지 모두 검색===============");
        System.out.println("전부");
        List<Message> messageList = messageService.findAll();
        messageList.forEach(System.out::println);

        System.out.println("하나만");
        System.out.println(messageService.findById(messageService.findByUserId(currentUser.getId()).get(0).getId()));

        System.out.println("\n===============메시지 정보 수정===============");
        for (int i = 0; i < 3; i++) {
            Message temp = messageList.get(i);
            System.out.println("nickname : "+ temp.getUser().getNickname());
            if (temp.getUser().getId().equals(currentUser.getId())){
                System.out.println("+++++기존 내용+++++");
                System.out.println(temp.getContent());
                System.out.println("++++++++++++++++++");
                System.out.println("메시지를 수정하세요.");
                String editedMessage = "메시지 수정";
                System.out.println(editedMessage + "\n++++++++++++++++++");

                System.out.println("전 : " + temp);
                messageService.update(temp.getId(),currentUser.getId(),
                        editedMessage);
                System.out.println("후 : " + temp);
                System.out.println();
            }
            else System.out.println("수정할 권한이 없습니다.");
            messageService.findAll().forEach(System.out::println);
            System.out.println();
        }

        System.out.println("\n===============메시지 삭제===============");
        for (Iterator<Message> it = messageService.findAll().iterator(); it.hasNext(); ) {
            Message temp = it.next();

            System.out.println("++++++++++");
            System.out.println("nickname : "+ temp.getUser().getNickname());
            if (temp.getUser().getId().equals(currentUser.getId())){
                messageService.delete(temp.getId(), currentUser.getId());
                System.out.println("삭제 완료.\n");
            }
            else System.out.println("수정할 권한이 없습니다.\n");
            messageService.findAll().forEach(System.out::println);
            System.out.println();
        }

    }
}
