package ua.kr.accounting.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "income_expenses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncomeExpense {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id")
     private Long id;
     @Column(name = "description", columnDefinition = "text")
     private String description;
     @Column(name = "amount")
     private double amount;
     @Column(name = "category")
     private String category;
     @Column(name = "date")
     private String date;
     @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
     @JoinColumn
     private User user;
}
