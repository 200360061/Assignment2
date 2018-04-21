/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package store;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Product {

    String Name;
    int price;

    // Predicate lambda expression to check if string is empty or not
    Predicate<String> productName = (pName) -> pName.isEmpty();
    // Consumer lambda expession to print string in uppercase
    Consumer<String> consumerExpression = (argument) -> System.out.println(argument.toUpperCase());
    // Function lambda expression to convert last digit of price to nearest of 0 or 5
    Function<Integer, Integer> roundOfPrice = (pPrice)
            -> {
        int x = pPrice % 10;
        if (x > 5) {
            pPrice = pPrice / 10;
            pPrice++;
            pPrice = pPrice * 10;
        } else {
            pPrice = pPrice / 10;
            pPrice = pPrice * 10;
        }
        return pPrice;
    };

    /**
     * Constructors to initialize variables
     *
     * @param Name
     * @param price
     */
    public Product(String Name, int price) {
        setName(Name);
        setPrice(price);
    }

    /**
     * Method to return Name
     *
     * @return
     */
    public String getName() {
        return Name;
    }

    /**
     * Method to set Product name using validations
     *
     * @param Name
     */
    public void setName(String Name) {
        if (!productName.test(Name)) {
            this.Name = Name;
        } else {
            throw new IllegalArgumentException("Name Field Cannot be empty");
        }
    }

    /**
     * Method to return Price
     *
     * @return
     */
    public int getPrice() {
        return price;
    }

    /**
     * Method to set Product Price using validations
     *
     * @param Name
     */
    public void setPrice(int price) {
        if (price > 0) {
            this.price = roundOfPrice.apply(price);
        } else {
            throw new IllegalArgumentException("Price cannot be less than 0");
        }
    }

    /**
     * Overridden method toString
     *
     * @return
     */
    public String toString() {
        return String.format("Product Name : %30s   |  Product Price : %30s", Name, price);
    }

    /**
     * Method to show product details in uppercase
     */
    public void showProductdetail() {
        consumerExpression.accept("Product Name : " + Name + "\nProduct Price : " + price);
    }
}
