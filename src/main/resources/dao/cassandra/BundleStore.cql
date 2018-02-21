CREATE TYPE IF NOT EXISTS bundles.Address_Type (
    id            text,
    address_one   text,
    address_two   text,
    state         text,
    city          text,
    country       text,
    postcode      text
);

CREATE TABLE IF NOT EXISTS bundles.User (
    id            text,
    email         text,
    password      text,
    firstName     text,
    lastName      text,
    groupType     text,
    lastAccess    timestamp,
    dateModified  timestamp,
    dateCreated     timestamp,
    modifiedBy    text,
    gender        text,
    dateOfBirth   timestamp,
    addresses     list<frozen<Address_Type>>,
    phoneNumber   text,
    subscriber    boolean,
    orders        set<text>,
    shoppingCartId        text,
    PRIMARY KEY (id, email)
) WITH
    bloom_filter_fp_chance=0.001000 AND
        caching={'keys': 'ALL' } AND
        comment='' AND
        dclocal_read_repair_chance=0.100000 AND
        gc_grace_seconds=10800 AND
        read_repair_chance=0.050000 AND
        default_time_to_live=0 AND
        speculative_retry='99.0PERCENTILE' AND
        memtable_flush_period_in_ms=0 AND
        compaction={'class': 'LeveledCompactionStrategy'} AND
        compression={'sstable_compression': 'LZ4Compressor'};

CREATE TYPE IF NOT EXISTS bundles.Price_Type (
    originPrice         double,
    salePrice           double
);

CREATE TYPE IF NOT EXISTS bundles.Product_Image_Type (
    id              text,
    description     text,
    productImage    text,
    smallImageUrl      text,
    defaultImage    boolean,
    imageType       text,
    imageUrl        text,
    imageCrop       boolean
);

CREATE TABLE IF NOT EXISTS bundles.Product (
    id            text,
    name          text,
    description   text,
    category      text,
    sizes         set<int>,
    textures      set<text>,
    colors        set<text>,
    onSale        boolean,
    saleNum       int,
    stockNum      int,
    prices        map<text, frozen<Price_Type>>,
    priceRule     text,
    productImages   list<frozen<Product_Image_Type>>,
    reviews       map<text, text>,
    PRIMARY KEY (id)
) WITH
   bloom_filter_fp_chance=0.001000 AND
       caching={'keys': 'ALL' } AND
       comment='' AND
       dclocal_read_repair_chance=0.100000 AND
       gc_grace_seconds=10800 AND
       read_repair_chance=0.050000 AND
       default_time_to_live=0 AND
       speculative_retry='99.0PERCENTILE' AND
       memtable_flush_period_in_ms=0 AND
       compaction={'class': 'LeveledCompactionStrategy'} AND
       compression={'sstable_compression': 'LZ4Compressor'};

CREATE TYPE IF NOT EXISTS bundles.ShoppingCart_Item_Type (
    id             text,
    productId      text,
    quantity       int,
    price          float,
    name           text,
    salePrice      float,
    description    text
);

CREATE TABLE IF NOT EXISTS bundles.Orders (
    id               text,
    items            list<frozen<ShoppingCart_Item_Type>>,
    userId           text,
    address          text,
    phone            text,
    orderDate        timestamp,
    deliverFee       float,
    payableFee       float,
    totalPrice       float,
    taxFee           float,
    trackingNum      text,
    employeeName     text,
    status           text,
    shippingMethod   text,
    deliverStatus    text,
    PRIMARY KEY (id)
) WITH
    bloom_filter_fp_chance=0.001000 AND
        caching={'keys': 'ALL' } AND
        comment='' AND
        dclocal_read_repair_chance=0.100000 AND
        gc_grace_seconds=10800 AND
        read_repair_chance=0.050000 AND
        default_time_to_live=0 AND
        speculative_retry='99.0PERCENTILE' AND
        memtable_flush_period_in_ms=0 AND
        compaction={'class': 'LeveledCompactionStrategy'} AND
        compression={'sstable_compression': 'LZ4Compressor'};

CREATE TABLE IF NOT EXISTS bundles.Shopping_Cart (
    id               text,
    items            list<frozen<ShoppingCart_Item_Type>>,
    address          text,
    userId           text,
    PRIMARY KEY (id)
) WITH
    bloom_filter_fp_chance=0.001000 AND
        caching={'keys': 'ALL' } AND
        comment='' AND
        dclocal_read_repair_chance=0.100000 AND
        gc_grace_seconds=10800 AND
        read_repair_chance=0.050000 AND
        default_time_to_live=0 AND
        speculative_retry='99.0PERCENTILE' AND
        memtable_flush_period_in_ms=0 AND
        compaction={'class': 'LeveledCompactionStrategy'} AND
        compression={'sstable_compression': 'LZ4Compressor'};

CREATE TABLE IF NOT EXISTS bundles.Invoice (
    id               text,
    orderId         text,
    orderDate        timestamp,
    amountPaid       float,
    amountDue        float,
    dueDate          timestamp,
    PRIMARY KEY (id)
) WITH
    bloom_filter_fp_chance=0.001000 AND
        caching={'keys': 'ALL' } AND
        comment='' AND
        dclocal_read_repair_chance=0.100000 AND
        gc_grace_seconds=10800 AND
        read_repair_chance=0.050000 AND
        default_time_to_live=0 AND
        speculative_retry='99.0PERCENTILE' AND
        memtable_flush_period_in_ms=0 AND
        compaction={'class': 'LeveledCompactionStrategy'} AND
        compression={'sstable_compression': 'LZ4Compressor'};

CREATE TABLE IF NOT EXISTS bundles.Coupon (
    id               text,
    name             text,
    code             text,
    discount         text,
    minOrderValue    float,
    createdAt        timestamp,
    validFrom        timestamp,
    validTo          timestamp,
    PRIMARY KEY (id)
) WITH
    bloom_filter_fp_chance=0.001000 AND
        caching={'keys': 'ALL' } AND
        comment='' AND
        dclocal_read_repair_chance=0.100000 AND
        gc_grace_seconds=10800 AND
        read_repair_chance=0.050000 AND
        default_time_to_live=0 AND
        speculative_retry='99.0PERCENTILE' AND
        memtable_flush_period_in_ms=0 AND
        compaction={'class': 'LeveledCompactionStrategy'} AND
        compression={'sstable_compression': 'LZ4Compressor'};