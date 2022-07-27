package com.viettel.vtskit.maria.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class PageImpl<T> implements Page<T> {
    private final long total;
    private final List<T> content = new ArrayList();
    private final Pageable pageable;

    public PageImpl(List<T> content, Pageable pageable, long total) {
        this.total = total;
        this.content.addAll(content);
        this.pageable = pageable;
    }

    public boolean isFirst() {
        return !this.hasPrevious();
    }

    public boolean hasPrevious() {
        return this.getNumber() > 0;
    }

    public Pageable nextPageable() {
        return this.hasNext() ? this.pageable.next() : Pageable.unpaged();
    }

    public Pageable previousPageable() {
        return this.hasPrevious() ? this.pageable.previousOrFirst() : Pageable.unpaged();
    }

    @Override
    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        return null;
    }

    public boolean hasContent() {
        return !this.content.isEmpty();
    }

    public List<T> getContent() {
        return Collections.unmodifiableList(this.content);
    }

    public Pageable getPageable() {
        return this.pageable;
    }

    public Sort getSort() {
        return this.pageable.getSort();
    }

    public Iterator<T> iterator() {
        return this.content.iterator();
    }

    public int getSize() {
        return this.pageable.isPaged() ? this.pageable.getPageSize() : this.content.size();
    }

    public int getNumberOfElements() {
        return this.content.size();
    }

    public int getTotalPages() {
        return this.getSize() == 0 ? 1 : (int)Math.ceil((double)this.total / (double)this.getSize());
    }

    public long getTotalElements() {
        return this.total;
    }

    public boolean hasNext() {
        return this.getNumber() + 1 < this.getTotalPages();
    }

    public boolean isLast() {
        return !this.hasNext();
    }

    public int getNumber() {
        return this.pageable.isPaged() ? this.pageable.getPageNumber() : 0;
    }
}
