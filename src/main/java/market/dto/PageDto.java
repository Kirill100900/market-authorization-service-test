package market.dto;

import java.util.List;

public class PageDto {

    private Long totalEntitiesCount;
    private Integer currentPage;
    private Integer pageCount;
    private Integer countOnPage;
    private List<AccountDto> entities;

    public Long getTotalEntitiesCount() {
        return totalEntitiesCount;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public Integer getCountOnPage() {
        return countOnPage;
    }

    public List<AccountDto> getEntities() {
        return entities;
    }

    public void setTotalEntitiesCount(Long totalEntitiesCount) {
        this.totalEntitiesCount = totalEntitiesCount;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public void setCountOnPage(Integer countOnPage) {
        this.countOnPage = countOnPage;
    }

    public void setEntities(List<AccountDto> entities) {
        this.entities = entities;
    }
}