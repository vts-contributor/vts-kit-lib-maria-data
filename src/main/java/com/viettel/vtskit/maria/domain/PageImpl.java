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
    private final List<T> content = new ArrayList<>();
    private final Pageable pageable;

    public PageImpl(List<T> content, Pageable pageable, long total) {
        this.total = total;
        this.content.addAll(content);
        this.pageable = pageable;
    }

    @Override
    public boolean isFirst() {
        return !this.hasPrevious();
    }

    @Override
    public boolean hasPrevious() {
        return this.getNumber() > 0;
    }

    @Override
    public Pageable nextPageable() {
        return this.hasNext() ? this.pageable.next() : Pageable.unpaged();
    }

    @Override
    public Pageable previousPageable() {
        return this.hasPrevious() ? this.pageable.previousOrFirst() : Pageable.unpaged();
    }

    @Override
    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        return null;
    }

    @Override
    public boolean hasContent() {
        return !this.content.isEmpty();
    }

    @Override
    public List<T> getContent() {
        return Collections.unmodifiableList(this.content);
    }

    @Override
    public Pageable getPageable() {
        return this.pageable;
    }

    @Override
    public Sort getSort() {
        return this.pageable.getSort();
    }

    @Override
    public Iterator<T> iterator() {
        return this.content.iterator();
    }

    @Override
    public int getSize() {
        return this.pageable.isPaged() ? this.pageable.getPageSize() : this.content.size();
    }

    @Override
    public int getNumberOfElements() {
        return this.content.size();
    }

    @Override
    public int getTotalPages() {
        return this.getSize() == 0 ? 1 : (int)Math.ceil((double)this.total / (double)this.getSize());
    }

    @Override
    public long getTotalElements() {
        return this.total;
    }

    @Override
    public boolean hasNext() {
        return this.getNumber() + 1 < this.getTotalPages();
    }

    @Override
    public boolean isLast() {
        return !this.hasNext();
    }

    @Override
    public int getNumber() {
        return this.pageable.isPaged() ? this.pageable.getPageNumber() : 0;
    }
}
