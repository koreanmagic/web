package kr.co.koreanmagic.hibernate3.conversation;

import org.hibernate.classic.Session;

public interface ConversationManager {
	
	// 세션을 가지고 온다.
	Session getBindSession();
	Session unbind();
	
	// 컨버세이션을 종료한다.
	Session complate();
	
}
