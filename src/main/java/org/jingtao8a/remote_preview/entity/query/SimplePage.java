package org.jingtao8a.remote_preview.entity.query;
import org.jingtao8a.remote_preview.enums.PageSize;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SimplePage {
    private Long pageNo;
    private Long countTotal;
    private Long pageSize;
    private Long pageTotal;
    private Long start;
    private Long end;

    public SimplePage(Long pageNo, Long countTotal, Long pageSize) {
        if (pageNo == null) {
            pageNo = 0L;
        }
        this.pageNo = pageNo;
        this.countTotal = countTotal;
        this.pageSize = pageSize;
        action();
    }
    public SimplePage(Long start, Long end) {
        this.start = start;
        this.end = end;
    }

    public void action() {
        if (pageSize <= 0) {
            pageSize = (long)PageSize.SIZE20.getSize();
        }
        if (countTotal > 0) {
            pageTotal = countTotal % pageSize == 0 ? countTotal / pageSize : countTotal / pageSize + 1;
        } else {
            pageTotal = 1L;
        }
        if (pageNo <= 1) {
            pageNo = 1L;
        }
        if (pageNo > pageTotal) {
            pageNo = pageTotal;
        }
        start = pageSize * (pageNo - 1);
        end = Math.min(pageSize * pageNo, countTotal);
    }

    public void setCountTotal(Long countTotal) {
        this.countTotal = countTotal;
        action();
    }
}
