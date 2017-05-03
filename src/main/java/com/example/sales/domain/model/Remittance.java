package com.example.sales.domain.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Rybachello on 5/3/2017.
 */
@Entity
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode
public class Remittance {

    @Id
    String id;

    String note;

    //todo: PurchaseOrderID
}
