package com.eucaliptus.springboot_app_products.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BatchId implements Serializable {
    private String idProduct;
    private Date batch;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BatchId batchId = (BatchId) o;
        return Objects.equals(idProduct, batchId.idProduct) &&
                Objects.equals(batch, batchId.batch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProduct, batch);
    }
}
