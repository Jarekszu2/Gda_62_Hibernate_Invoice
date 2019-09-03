package packInvoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class InvoicePosition implements IBaseEntity{
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String positionName;
    @Column(nullable = false)
    private int positionQuantity;
    @Column(nullable = false)
    private double positionAmount;
    @Column(nullable = false)
    private double taxRate;
//    @Formula(value = "(positionQuantity * positionAmount * taxRate)")
//    private double taxAmount;

//    private double positionTotalAmount;
    @ToString.Exclude
    @ManyToOne()
    private Invoice invoice;
}
