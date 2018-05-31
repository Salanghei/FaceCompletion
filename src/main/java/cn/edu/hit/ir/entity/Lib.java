package cn.edu.hit.ir.entity;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table lib
 *
 * @mbg.generated do_not_delete_during_merge
 */
public class Lib {
    /**
     * Database Column Remarks:
     *   资料id，主键
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lib.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   用户id
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lib.user_id
     *
     * @mbg.generated
     */
    private Integer userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lib.robot_id
     *
     * @mbg.generated
     */
    private Integer robotId;

    /**
     * Database Column Remarks:
     *   文件路径
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lib.path
     *
     * @mbg.generated
     */
    private String path;

    private Integer level;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lib
     *
     * @mbg.generated
     */
    public Lib(Integer id, Integer userId, Integer robotId, String path,Integer level) {
        this.id = id;
        this.userId = userId;
        this.robotId = robotId;
        this.path = path;
        this.level = level;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lib
     *
     * @mbg.generated
     */
    public Lib() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lib.id
     *
     * @return the value of lib.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lib.id
     *
     * @param id the value for lib.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lib.user_id
     *
     * @return the value of lib.user_id
     *
     * @mbg.generated
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lib.user_id
     *
     * @param userId the value for lib.user_id
     *
     * @mbg.generated
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lib.robot_id
     *
     * @return the value of lib.robot_id
     *
     * @mbg.generated
     */
    public Integer getRobotId() {
        return robotId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lib.robot_id
     *
     * @param robotId the value for lib.robot_id
     *
     * @mbg.generated
     */
    public void setRobotId(Integer robotId) {
        this.robotId = robotId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lib.path
     *
     * @return the value of lib.path
     *
     * @mbg.generated
     */
    public String getPath() {
        return path;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lib.path
     *
     * @param path the value for lib.path
     *
     * @mbg.generated
     */
    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }


    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Lib{" +
                "id=" + id +
                ", userId=" + userId +
                ", robotId=" + robotId +
                ", path='" + path + '\'' +
                ", level=" + level +
                '}';
    }
}