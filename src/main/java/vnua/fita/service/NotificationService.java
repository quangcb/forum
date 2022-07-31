package vnua.fita.service;

import java.util.List;

import vnua.fita.entity.Notification;

public interface NotificationService {
	
	/** find info parent user by account **/
	public List<Notification> notify(String studentCode);
	
	public String count(String studentCode);
	
	public boolean insert(Notification notification);
	
	public boolean updateNotification(String studentCode);
}
