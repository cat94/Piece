package nju.com.piece.logic.tasktiming;


/**
 * 为任务计时的业务逻辑接口
 * 包含直接添加一段时间，开始正计时，开始倒计时、结束计时4个方法
 * @author Hyman
 */

public interface StartTaskSer {
	
	/**
	 * 直接添加一段时间
	 * @param taskId  任务的ID
	 * @param minutes 分钟数
	 * @return 是否添加成功 (在其中要加入一些逻辑判断，是否与之前的记录相冲突，比如5分钟前是在看书，现在添加6分钟的打球的记录就不行)
	 */
    public boolean addRecord(int taskId, int minutes);
    
    /**
     * 开始为某个任务正计时
     * @param taskId  任务的ID
     * @return 操作是否成功
     */
    public boolean timing(int taskId);
    
    /**
     * 开始为某个任务倒计时
     * @param taskId  任务的ID
     * @param minutes 分钟数
     * @return 操作是否成功
     */
    public boolean countDown(int taskId, int minutes);
    
    /**
     * 结束为某个任务计时
     * @param taskId  任务的ID
     * @return 计时秒数
     */
    public int stop(int taskId);
    
}
