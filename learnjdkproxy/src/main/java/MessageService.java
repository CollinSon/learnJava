/**
 * @author 许瑞锐
 * @date 2020/9/9 14:51
 * @description {java类描述}
 */
public class MessageService implements IMessageService{
    @Override
    public void sendMessage(String messsage) {
        System.out.println("send a message now : "+messsage);
    }
}
