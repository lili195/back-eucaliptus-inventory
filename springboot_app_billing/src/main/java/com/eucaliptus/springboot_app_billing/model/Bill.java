package com.eucaliptus.springboot_app_billing.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "bills")

public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bill")
    private Integer idBill;

    @Column(name = "bill_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date billDate;

    @Column(name = "total", nullable = false)
    private Double total;

    @Column(name = "id_person", nullable = false)
    private Integer idPerson;  // Llave foránea de otro microservicio

    @ManyToOne
    @JoinColumn(name = "id_client", referencedColumnName = "id_client", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "bill")
    private List<Sale> sales;

    public Bill(Date billDate, Double total, Integer idPerson, Client client) {
        this.billDate = billDate;
        this.total = total;
        this.idPerson = idPerson;
        this.client = client;
    }

    public Object getIdClient() {
        return null;
    }

    public void setIdClient(Object idClient) {
    }
}