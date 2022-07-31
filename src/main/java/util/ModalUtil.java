package util;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Window;

public class ModalUtil {

	public static void showModalLogin(Event e) {
        Window window = (Window)Executions.createComponents(
                "/components/modal-login.zul", null, null);
        window.doModal();
    }
}
