package packInvoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Invoice implements IBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @CreationTimestamp
    private LocalDateTime dateAdded;
    @Column(columnDefinition = "tinyint default 0")
//    @Column(nullable = false)
    private boolean paid;
    private LocalDateTime dateRelased;
    private LocalDateTime dateOfPayment;

    @Formula(value = "(select sum((inPo.positionQuantity * (inPo.positionAmount * (1 + inPo.taxRate)))) from invoiceposition inPo where inPo.invoice_id = id)")
    private Double amount;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "invoice", fetch = FetchType.EAGER)
    private Set<InvoicePosition> invoicePositionSet;
}
