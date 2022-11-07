package com.sherpa.carrier_sherpa.domain.entity;

import com.sherpa.carrier_sherpa.domain.enums.ReportType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Table(name = "report")
@Entity
public class Report  extends BaseEntity{

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "report_id")
//    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    private ReportType reportType;

    private String details;

    @Builder
    public Report( Order order, Member sender, Member courier, ReportType reportType, String details) {
        this.order = order;
        this.reportType = reportType;
        this.details = details;
    }

    public Report( String id,Order order, Member sender, Member courier, ReportType reportType, String details) {
        super(id);
        this.reportType = reportType;
        this.details = details;
    }
}
