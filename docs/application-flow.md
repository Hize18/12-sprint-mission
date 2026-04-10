Application-flow

Spring 마이그레이션 후

기존 app내용은 이전 커밋 참고.

기존 JavaApplication 파일은 스프링 실행파일과 비교를 위해 이번 커밋까지만 유지.

```aiignore
JavaApplication

UserRepository ur = new FileUserRepository();
ChannelRepository cr = new FileChannelRepository();
MessageRepository mr = new FileMessageRepository();

UserService us = new BasicUserService(ur);
ChannelService cs = new BasicChannelService(cr, ur);
MessageService ms = new BasicMessageService(mr, cr, ur);
```

```aiignore
DiscodeitApplication

UserService us = context.getBean(UserService.class);
ChannelService cs = context.getBean(ChannelService.class);
MessageService ms = context.getBean(MessageService.class);
```

- 스프링 이전 구조에서는 Repository와 Service 객체를 직접 생성하고 의존성을 주입해야 했다.
- 따라서 Repository를 직접 생성한 뒤 Service에 필요한 의존성을 수동으로 주입하는 방식으로 구성되었다.

- 하지만 스프링으로 마이그레이션한 이후에는 의존성 주입을 스프링 컨테이너가 담당하게 되었으며,
- 객체 생성 및 의존성 관리의 책임이 애플리케이션 코드에서 스프링 컨테이너로 이동하였다.