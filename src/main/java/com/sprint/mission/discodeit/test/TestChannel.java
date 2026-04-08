package com.sprint.mission.discodeit.test;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.Iterator;
import java.util.List;

public class TestChannel {
    public static void main(String[] args) {
//        System.in 생략
        UserService userService = new JCFUserService();
        for (int i = 0; i < 3; i++) {
            userService.save(new User(("test" + i), ("email" + i + "@test.co"),String.valueOf(i).repeat(4), ("nickname" + i)));
        }
        User currentUser = userService.findAll().get(0);

        System.out.println("---------------채널 테스트---------------");
        System.out.println("===============채널 입력부===============");
        ChannelService channelService = new JCFChannelService();
        for (int i = 0; i < 3; i++) {
            String name = "test_channel_"+i;
            channelService.save(new Channel(currentUser,name, channelService.createHandle(name)));
        }

        System.out.println("\n===============채널 단일 검색===============");
        System.out.println(channelService.findById(channelService.findByOwner(currentUser.getId()).get(0).getId()));

        System.out.println("\n===============채널 전부 검색===============");
        channelService.findAll().forEach(System.out::println);

        System.out.println("\n===============채널 정보 수정===============");
        List<Channel> channelList = channelService.findAll();

        System.out.print("채널 3개를 순회하면서 수정");
        channelList.forEach(System.out::println);

        for (int i = 0; i < 3; i++) {
            if (channelList.get(i).getOwner().getId().equals(currentUser.getId())){
                System.out.println("+++++기존 내용+++++");
                System.out.println(channelList.get(i).getName());
                System.out.println("++++++++++++++++++");
                System.out.println("채널 이름을 수정하세요.");
                String editedMessage = "채널 이름 수정";
                System.out.println(editedMessage + "\n++++++++++++++++++");

                System.out.println("전 : " + channelList.get(i));
                channelService.update(currentUser.getId(), channelList.get(i).getId(), editedMessage);
                System.out.println("후 : " + channelList.get(i));
            }
            else System.out.println("수정할 권한이 없습니다.");
            channelList.forEach(System.out::println);
        }

        System.out.println("\n===============채널 삭제===============");
        System.out.print("채널 순회하면서 채널 삭제");

        for (Iterator<Channel> it = channelService.findAll().iterator(); it.hasNext(); ) {
            Channel channel = it.next();
            System.out.println("++++++++++");
            System.out.println("현재 channel : "+channel.getName());

            if (channel.getOwner().getId().equals(currentUser.getId())){
                channelService.delete(currentUser.getId(), channel.getId());
                System.out.println("삭제 완료");
            }
            else System.out.println("삭제할 권한이 없습니다.");
            channelService.findAll().forEach(System.out::println);
            System.out.println();
        }
    }
}
