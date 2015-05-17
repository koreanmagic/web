package kr.co.koreanmagic.hibernate3.conversation;

import org.hibernate.classic.Session;

public interface ConversationExecutor {

	// 컨버세이션 확장에 의해 바인딩된 세션을 받아 작업
	// boolean값을 통해 컨버세션을 종료할지 결정
	boolean execute(Session session);
}
