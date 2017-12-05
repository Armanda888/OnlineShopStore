package com.theBeautiful.cassandra.model;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Frozen;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.google.common.collect.Lists;
import com.theBeautiful.model.Address;
import com.theBeautiful.model.User;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Table(keyspace = "bundles", name = "User")
public class UserEntity implements DBEntityInterface<User>{

    @PartitionKey(0)
    private String id;

    /* user login email */
    @PartitionKey(1)
    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String groupType;

    private Date lastAccess;

    private Date dateModified;

    private Date dateCreated;

    private String modifiedBy;

    private String gender;

    private Date dateOfBirth;

    @Frozen("list<frozen<AddressType>>")
    private List<AddressType> addresses;

    private String phoneNumber;

    /* if the user subscribed the newsletter or not. */
    private Boolean subscriber;

    private Set<String> orders;

    private String shoppingCartId;

    public UserEntity() {
    }

    public UserEntity(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        if (user.getGroupType() != null) {
            this.groupType = String.valueOf(user.getGroupType());
        }
        this.lastAccess = user.getLastAccess();
        if (user.getDateModified() != null) {
            this.dateModified = user.getDateModified();
        }
        if (user.getDateCreated() != null) {
            this.dateCreated = user.getDateCreated();
        }
        this.modifiedBy = user.getModifiedBy();
        if (user.getGender() != null) {
            this.gender = String.valueOf(user.getGender());
        }
        if (user.getDateOfBirth() != null) {
            this.dateOfBirth = user.getDateOfBirth();
        }
        if (user.getPhoneNumber() != null) {
            this.phoneNumber = user.getPhoneNumber();
        }
        this.subscriber = user.getSubscriber();
        if (user.getOrders() != null) {
            this.orders = user.getOrders();
        }
        if (user.getAddresses() != null) {
            addresses = Lists.newArrayList();
            for (Address address : user.getAddresses()) {
                addresses.add(new AddressType(address));
            }
        }
        this.shoppingCartId = user.getShoppingCartId();
    }


    @Override
    public User generate() {
        User user = new User();
        user.setId(this.id);
        user.setPassword(this.password);
        user.setEmail(this.email);
        if (this.firstName != null) {
            user.setFirstName(this.firstName);
        }
        if (this.lastName != null) {
            user.setLastName(this.lastName);
        }
        if (this.groupType != null) {
            user.setGroupType(User.GroupType.valueOf(this.groupType));
        }
        if (this.lastAccess != null) {
            user.setLastAccess(this.lastAccess);
        }
        if (this.dateModified != null) {
            user.setDateModified(this.dateModified);
        }
        user.setDateCreated(this.dateCreated);
        if (this.modifiedBy != null) {
            user.setModifiedBy(this.modifiedBy);
        }
        if (this.gender != null) {
            user.setGender(User.Gender.valueOf(this.gender));
        }
        if (this.dateOfBirth != null) {
            user.setDateOfBirth(this.dateOfBirth);
        }

        if (this.phoneNumber != null) {
            user.setPhoneNumber(this.phoneNumber);
        }
        if (this.addresses != null && !this.addresses.isEmpty()) {
            List addressList = Lists.newArrayList();
            for (AddressType addressType : this.addresses) {
                addressList.add(addressType.generate());
            }
            user.setAddresses(addressList);
        }
        if (this.orders != null) {
            user.setOrders(this.orders);
        }
        user.setShoppingCartId(this.shoppingCartId);
        return user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<AddressType> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressType> addresses) {
        this.addresses = addresses;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Boolean subscriber) {
        this.subscriber = subscriber;
    }

    public Set<String> getOrders() {
        return orders;
    }

    public void setOrders(Set<String> orders) {
        this.orders = orders;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(String shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
