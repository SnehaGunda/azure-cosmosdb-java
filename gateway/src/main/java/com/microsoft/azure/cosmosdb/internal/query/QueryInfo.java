/*
 * The MIT License (MIT)
 * Copyright (c) 2018 Microsoft Corporation
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.microsoft.azure.cosmosdb.internal.query;

import java.util.Collection;
import java.util.List;

import com.microsoft.azure.cosmosdb.rx.internal.query.DistinctQueryType;
import org.apache.commons.lang3.StringUtils;

import com.microsoft.azure.cosmosdb.JsonSerializable;
import com.microsoft.azure.cosmosdb.internal.query.aggregation.AggregateOperator;

/**
 * Used internally to encapsulates a query's information in the Azure Cosmos DB database service.
 */
public final class QueryInfo extends JsonSerializable {
    private static final String HAS_SELECT_VALUE = "hasSelectValue";
    private Integer top;
    private List<SortOrder> orderBy;
    private Collection<AggregateOperator> aggregates;
    private Collection<String> orderByExpressions;
    private String rewrittenQuery;
    private Integer offset;
    private Integer limit;
    private DistinctQueryType distinctQueryType;

    public QueryInfo() { }

    public QueryInfo(String jsonString) {
        super(jsonString);
    }

    public Integer getTop() {
        return this.top != null ? this.top : (this.top = super.getInt("top"));
    }

    public List<SortOrder> getOrderBy() {
        return this.orderBy != null ? this.orderBy : (this.orderBy = super.getList("orderBy", SortOrder.class));
    }

    public String getRewrittenQuery() {
        return this.rewrittenQuery != null ? this.rewrittenQuery
                : (this.rewrittenQuery = super.getString("rewrittenQuery"));
    }

    public boolean hasTop() {
        return this.getTop() != null;
    }

    public boolean hasOrderBy() {
        Collection<SortOrder> orderBy = this.getOrderBy();
        return orderBy != null && orderBy.size() > 0;
    }

    public boolean hasRewrittenQuery() {
        return !StringUtils.isEmpty(this.getRewrittenQuery());
    }

    public boolean hasAggregates() {
        Collection<AggregateOperator> aggregates = this.getAggregates();
        return aggregates != null && aggregates.size() > 0;
    }

    public Collection<AggregateOperator> getAggregates() {
        return this.aggregates != null
                ? this.aggregates
                : (this.aggregates = super.getCollection("aggregates", AggregateOperator.class));
    }

    public Collection<String> getOrderByExpressions() {
        return this.orderByExpressions != null
                ? this.orderByExpressions
                : (this.orderByExpressions = super.getCollection("orderByExpressions", String.class));
    }

    public boolean hasSelectValue() {
        return super.has(HAS_SELECT_VALUE) && super.getBoolean(HAS_SELECT_VALUE);
    }

    public boolean hasOffset() {
        return this.getOffset() != null;
    }

    public boolean hasLimit() {
        return this.getLimit() != null;
    }

    public Integer getLimit() {
        return this.limit != null ? this.limit : (this.limit = super.getInt("limit"));
    }

    public Integer getOffset() {
        return this.offset != null ? this.offset : (this.offset = super.getInt("offset"));
    }

    public boolean hasDistinct() {
        return this.getDistinctQueryType() != DistinctQueryType.None;
    }

    public DistinctQueryType getDistinctQueryType() {
        if (distinctQueryType != null) {
            return distinctQueryType;
        } else {
            final String distinctType = super.getString("distinctType");
            switch (distinctType) {
                case "Ordered":
                    distinctQueryType = DistinctQueryType.Ordered;
                    break;
                case "Unordered":
                    distinctQueryType = DistinctQueryType.Unordered;
                    break;
                default:
                    distinctQueryType = DistinctQueryType.None;
                    break;
            }
            return distinctQueryType;
        }

    }
}