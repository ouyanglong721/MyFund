package com.oylong.myfund.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oylong.common.core.annotation.Excel;
import com.oylong.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 工单对象 ticket
 *
 * @author OyLong
 * @date 2021-12-27
 */
public class Ticket extends BaseEntity {
    private static final long serialVersionUID = 1L;

    public static final Integer STATUS_PROCESSING = 0;
    public static final Integer STATUS_REPLIED = 1;

    /**
     * ID
     */
    @Excel(name = "ID")
    private Long ticketId;

    /**
     * 标题
     */
    @Excel(name = "标题")
    private String ticketTitle;

    /**
     * 类型
     */
    @Excel(name = "类型")
    private Long ticketType;

    /**
     * 内容
     */
    private String ticketContent;

    /**
     * 所属企业
     */
    @Excel(name = "所属企业")
    private Long ticketEnterprise;

    /**
     * 优先级
     */
    private Integer ticketPriority;

    /**
     * 处理人
     */
    private Long ticketHandler;

    public Long getTicketType() {
        return ticketType;
    }

    public void setTicketType(Long ticketType) {
        this.ticketType = ticketType;
    }

    /**
     * 发起人
     */
    @Excel(name = "发起人")
    private Long ticketUser;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private Integer ticketStatus;

    /**
     * 发起时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发起时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date ticketCreateTime;

    /**
     * 工单修改时间
     */
    private Date ticketModifyTime;

    /**
     * 逻辑删除字段
     */
    private Integer delete;

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketTitle(String ticketTitle) {
        this.ticketTitle = ticketTitle;
    }

    public String getTicketTitle() {
        return ticketTitle;
    }

    public void setTicketContent(String ticketContent) {
        this.ticketContent = ticketContent;
    }

    public String getTicketContent() {
        return ticketContent;
    }

    public void setTicketEnterprise(Long ticketEnterprise) {
        this.ticketEnterprise = ticketEnterprise;
    }

    public Long getTicketEnterprise() {
        return ticketEnterprise;
    }

    public void setTicketPriority(Integer ticketPriority) {
        this.ticketPriority = ticketPriority;
    }

    public Integer getTicketPriority() {
        return ticketPriority;
    }

    public void setTicketHandler(Long ticketHandler) {
        this.ticketHandler = ticketHandler;
    }

    public Long getTicketHandler() {
        return ticketHandler;
    }

    public void setTicketUser(Long ticketUser) {
        this.ticketUser = ticketUser;
    }

    public Long getTicketUser() {
        return ticketUser;
    }

    public void setTicketStatus(Integer ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public Integer getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketCreateTime(Date ticketCreateTime) {
        this.ticketCreateTime = ticketCreateTime;
    }

    public Date getTicketCreateTime() {
        return ticketCreateTime;
    }

    public void setTicketModifyTime(Date ticketModifyTime) {
        this.ticketModifyTime = ticketModifyTime;
    }

    public Date getTicketModifyTime() {
        return ticketModifyTime;
    }

    public void setDelete(Integer delete) {
        this.delete = delete;
    }

    public Integer getDelete() {
        return delete;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("ticketId", getTicketId())
                .append("ticketTitle", getTicketTitle())
                .append("ticketContent", getTicketContent())
                .append("ticketEnterprise", getTicketEnterprise())
                .append("ticketPriority", getTicketPriority())
                .append("ticketHandler", getTicketHandler())
                .append("ticketUser", getTicketUser())
                .append("ticketStatus", getTicketStatus())
                .append("ticketCreateTime", getTicketCreateTime())
                .append("ticketModifyTime", getTicketModifyTime())
                .append("delete", getDelete())
                .toString();
    }
}
