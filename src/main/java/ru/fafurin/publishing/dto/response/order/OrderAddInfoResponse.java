package ru.fafurin.publishing.dto.response.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.fafurin.publishing.dto.response.BookResponse;
import ru.fafurin.publishing.dto.response.CustomerResponse;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderAddInfoResponse {
    OrderResponse order;
    BookResponse book;
    CustomerResponse customer;
}
