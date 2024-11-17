package my.learn.thinking.in.spring.ioc.overview.domain;

import my.learn.thinking.in.spring.ioc.overview.annotation.SuperU;

/**
 * 超级用户
 */
@SuperU
public class Super extends User{
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Super{" +
                "address='" + address + '\'' +
                "} " + super.toString();
    }
}
