package com.womai.mobile.api.service.common.redis;

import com.womai.mobile.api.domain.redis.Pair;
import redis.clients.jedis.JedisPubSub;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jijun.yang
 * Date: 14-1-2
 * Time: 下午7:49
 * To change this template use File | Settings | File Templates.
 */
public interface RedisObjectService {

    /**
     * 删除模糊匹配的key
     * @param likeKey 模糊匹配的key
     * @return 删除成功的条数
     */
    public long delKeysLike(final String likeKey);

    /**
     * 删除
     * @param key 匹配的key
     * @return 删除成功的条数
     */
    public Long delKey(final String key) ;

    /**
     * 删除
     * @param keys 匹配的key的集合
     * @return 删除成功的条数
     */
    public Long delKeys(final String[] keys) ;

    /**
     * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。
     * 在 Redis 中，带有生存时间的 key 被称为『可挥发』(volatile)的。
     * @param key key
     * @param expire 生命周期，单位为秒
     * @return 1: 设置成功 0: 已经超时或key不存在
     */
    public Long expire(final String key, final int expire) ;

    /**
     * 一个跨jvm的id生成器，利用了redis原子性操作的特点
     * @param key id的key
     * @return 返回生成的Id
     */
    public long makeId(final String key) ;

    /* ======================================Strings====================================== */

    /**
     * 将字符串值 value 关联到 key 。
     * 如果 key 已经持有其他值， setString 就覆写旧值，无视类型。
     * 对于某个原本带有生存时间（TTL）的键来说， 当 setString 成功在这个键上执行时， 这个键原有的 TTL 将被清除。
     * 时间复杂度：O(1)
     * @param key key
     * @param value string value
     * @return 在设置操作成功完成时，才返回 OK 。
     */
    public String setString(final String key, final String value);

    /**
     * 将值 value 关联到 key ，并将 key 的生存时间设为 expire (以秒为单位)。
     * 如果 key 已经存在， 将覆写旧值。
     * 类似于以下两个命令:
     * SET key value
     * EXPIRE key expire # 设置生存时间
     * 不同之处是这个方法是一个原子性(atomic)操作，关联值和设置生存时间两个动作会在同一时间内完成，在 Redis 用作缓存时，非常实用。
     * 时间复杂度：O(1)
     * @param key key
     * @param value string value
     * @param expire 生命周期
     * @return 设置成功时返回 OK 。当 expire 参数不合法时，返回一个错误。
     */
    public String setString(final String key, final String value, final int expire) ;

    /**
     * 将 key 的值设为 value ，当且仅当 key 不存在。若给定的 key 已经存在，则 setStringIfNotExists 不做任何动作。
     * 时间复杂度：O(1)
     * @param key key
     * @param value string value
     * @return 设置成功，返回 1 。设置失败，返回 0 。
     */
    public Long setStringIfNotExists(final String key, final String value) ;

    /**
     * 返回 key 所关联的字符串值。如果 key 不存在那么返回特殊值 nil 。
     * 假如 key 储存的值不是字符串类型，返回一个错误，因为 getString 只能用于处理字符串值。
     * 时间复杂度: O(1)
     * @param key key
     * @return 当 key 不存在时，返回 nil ，否则，返回 key 的值。如果 key 不是字符串类型，那么返回一个错误。
     */
    public String getString(final String key) ;

    /**
     * 批量的 {@link #setString(String, String)}
     * @param pairs 键值对数组{数组第一个元素为key，第二个元素为value}
     * @return 操作状态的集合
     */
    public List<Object> batchSetString(final List<Pair<String, String>> pairs) ;

    /**
     * 批量的 {@link #getString(String)}
     * @param keys key数组
     * @return value的集合
     */
    public List<String> batchGetString(final String[] keys) ;

    /* ======================================Hashes====================================== */

    /**
     * 将哈希表 key 中的域 field 的值设为 value 。
     * 如果 key 不存在，一个新的哈希表被创建并进行 hashSet 操作。
     * 如果域 field 已经存在于哈希表中，旧值将被覆盖。
     * 时间复杂度: O(1)
     * @param key key
     * @param field 域
     * @param value string value
     * @return 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 1 。如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回 0 。
     */
    public Long hashSet(final String key, final String field, final String value) ;
    /**
     * 将哈希表 key 中的域 field 的值设为 value 。
     * 如果 key 不存在，一个新的哈希表被创建并进行 hashSet 操作。
     * 如果域 field 已经存在于哈希表中，旧值将被覆盖。
     * @param key key
     * @param field 域
     * @param value string value
     * @param expire 生命周期，单位为秒
     * @return 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 1 。如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回 0 。
     */
    public Long hashSet(final String key, final String field, final String value, final int expire) ;
    /**
     * 返回哈希表 key 中给定域 field 的值。
     * 时间复杂度:O(1)
     * @param key key
     * @param field 域
     * @return 给定域的值。当给定域不存在或是给定 key 不存在时，返回 nil 。
     */
    public String hashGet(final String key, final String field) ;

    /**
     * 返回哈希表 key 中给定域 field 的值。 如果哈希表 key 存在，同时设置这个 key 的生存时间
     * @param key key
     * @param field 域
     * @param expire 生命周期，单位为秒
     * @return 给定域的值。当给定域不存在或是给定 key 不存在时，返回 nil 。
     */
    public String hashGet(final String key, final String field, final int expire) ;

    /**
     * 同时将多个 field-value (域-值)对设置到哈希表 key 中。
     * 时间复杂度: O(N) (N为fields的数量)
     * @param key key
     * @param hash field-value的map
     * @return 如果命令执行成功，返回 OK 。当 key 不是哈希表(hash)类型时，返回一个错误。
     */
    public String hashMultipleSet(final String key, final Map<String, String> hash) ;

    /**
     * 同时将多个 field-value (域-值)对设置到哈希表 key 中。同时设置这个 key 的生存时间
     * @param key key
     * @param hash field-value的map
     * @param expire 生命周期，单位为秒
     * @return 如果命令执行成功，返回 OK 。当 key 不是哈希表(hash)类型时，返回一个错误。
     */
    public String hashMultipleSet(final String key, final Map<String, String> hash, final int expire);

    /**
     * 返回哈希表 key 中，一个或多个给定域的值。如果给定的域不存在于哈希表，那么返回一个 nil 值。
     * 时间复杂度: O(N) (N为fields的数量)
     * @param key key
     * @param fields field的数组
     * @return 一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样。
     */
    public List<String> hashMultipleGet(final String key, final String... fields);

    /**
     * 返回哈希表 key 中，一个或多个给定域的值。如果给定的域不存在于哈希表，那么返回一个 nil 值。
     * 同时设置这个 key 的生存时间
     * @param key key
     * @param fields field的数组
     * @param expire 生命周期，单位为秒
     * @return 一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样。
     */
    public List<String> hashMultipleGet(final String key, final int expire, final String... fields) ;

    /**
     * 批量的{@link #hashMultipleSet(String, java.util.Map)}，在管道中执行
     * @param pairs 多个hash的多个field
     * @return 操作状态的集合
     */
    public List<Object> batchHashMultipleSet(final List<Pair<String, Map<String, String>>> pairs);


    /**
     * 批量的{@link #hashMultipleSet(String, java.util.Map)}，在管道中执行
     * @param data Map<String, Map<String, String>>格式的数据
     * @return 操作状态的集合
     */
    public List<Object> batchHashMultipleSet(final Map<String, Map<String, String>> data) ;

    /**
     * 批量的{@link #hashMultipleGet(String, String...)}，在管道中执行
     * @param pairs 多个hash的多个field
     * @return 执行结果的集合
     */
    public List<List<String>> batchHashMultipleGet(final List<Pair<String, String[]>> pairs) ;

    /**
     * 返回哈希表 key 中，所有的域和值。在返回值里，紧跟每个域名(field name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。
     * 时间复杂度: O(N)
     * @param key key
     * @return 以列表形式返回哈希表的域和域的值。若 key 不存在，返回空列表。
     */
    public Map<String, String> hashGetAll(final String key);

    /**
     * 返回哈希表 key 中，所有的域和值。在返回值里，紧跟每个域名(field name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。
     * 同时设置这个 key 的生存时间
     * @param key key
     * @param expire 生命周期，单位为秒
     * @return 以列表形式返回哈希表的域和域的值。若 key 不存在，返回空列表。
     */
    public Map<String, String> hashGetAll(final String key, final int expire);

    /**
     * 批量的{@link #hashGetAll(String)}
     * @param keys key的数组
     * @return 执行结果的集合
     */
    public List<Map<String, String>> batchHashGetAll(final String... keys);

    /**
     * 批量的{@link #hashMultipleGet(String, String...)}，与{@link #batchHashGetAll(String...)}不同的是，返回值为Map类型
     * @param keys key的数组
     * @return 多个hash的所有filed和value
     */
    public Map<String, Map<String, String>> batchHashGetAllForMap(final String... keys);

    /**
     * 删除哈希表key中的一个或多个指定域，不存在的域将被忽略。
     * @param key key
     * @param fields field的数组
     * @return 删除的field个数。
     */
    public Long hashDel(final String key, final String... fields);
    /* ======================================List====================================== */

    /**
     * 将一个或多个值 value 插入到列表 key 的表尾(最右边)。
     * @param key key
     * @param values value的数组
     * @return 执行 listPushTail 操作后，表的长度
     */
    public Long listPushTail(final String key, final String... values) ;
    /**
     * 将一个或多个值 value 插入到列表 key 的表头
     * @param key key
     * @param value string value
     * @return 执行 listPushHead 命令后，列表的长度。
     */
    public Long listPushHead(final String key, final String value);
    /**
     * 将一个或多个值 value 插入到列表 key 的表头, 当列表大于指定长度是就对列表进行修剪(trim)
     * @param key key
     * @param value string value
     * @param size 链表超过这个长度就修剪元素
     * @return 执行 listPushHeadAndTrim 命令后，列表的长度。
     */
    public Long listPushHeadAndTrim(final String key, final String value, final long size) ;
    /**
     * 批量的{@link #listPushTail(String, String...)}，以锁的方式实现
     * @param key key
     * @param values value的数组
     * @param delOld 如果key存在，是否删除它。true 删除；false: 不删除，只是在行尾追加
     */
    public void batchListPushTail(final String key, final String[] values, final boolean delOld) ;

    /**
     * 同{@link #batchListPushTail(String, String[], boolean)},不同的是利用redis的事务特性来实现
     * @param key key
     * @param values value的数组
     * @return null
     */
    public Object updateListInTransaction(final String key, final List<String> values) ;

    /**
     * 在key对应list的尾部部添加字符串元素,如果key存在，什么也不做
     * @param key key
     * @param values value的数组
     * @return 执行insertListIfNotExists后，表的长度
     */
    public Long insertListIfNotExists(final String key, final String[] values) ;

    /**
     * 返回list所有元素，下标从0开始，负值表示从后面计算，-1表示倒数第一个元素，key不存在返回空列表
     * @param key key
     * @return list所有元素
     */
    public List<String> listGetAll(final String key) ;

    /**
     * 返回指定区间内的元素，下标从0开始，负值表示从后面计算，-1表示倒数第一个元素，key不存在返回空列表
     * @param key key
     * @param beginIndex 下标开始索引（包含）
     * @param endIndex 下标结束索引（不包含）
     * @return 指定区间内的元素
     */
    public List<String> listRange(final String key, final long beginIndex, final long endIndex) ;

    /**
     * 一次获得多个链表的数据
     * @param keys key的数组
     * @return 执行结果
     */
    public Map<String, List<String>> batchGetAllList(final List<String> keys) ;

    /* ======================================Pub/Sub====================================== */

    /**
     * 将信息 message 发送到指定的频道 channel。
     * 时间复杂度：O(N+M)，其中 N 是频道 channel 的订阅者数量，而 M 则是使用模式订阅(subscribed patterns)的客户端的数量。
     * @param channel 频道
     * @param message 信息
     * @return 接收到信息 message 的订阅者数量。
     */
    public Long publish(final String channel, final String message);

    /**
     * 订阅给定的一个频道的信息。
     * @param jedisPubSub 监听器
     * @param channel 频道
     */
    public void subscribe(final JedisPubSub jedisPubSub, final String channel) ;

    /**
     * 取消订阅
     * @param jedisPubSub 监听器
     */
    public void unSubscribe(final JedisPubSub jedisPubSub) ;
    /* ======================================Sorted set================================= */

    /**
     * 将一个 member 元素及其 score 值加入到有序集 key 当中。
     * @param key key
     * @param score score 值可以是整数值或双精度浮点数。
     * @param member 有序集的成员
     * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
     */
    public Long addWithSortedSet(final String key, final double score, final String member) ;

    /**
     * 将多个 member 元素及其 score 值加入到有序集 key 当中。
     * @param key key
     * @param scoreMembers score、member的pair
     * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
     */
    public Long addWithSortedSet(final String key, final Map<String, Double> scoreMembers) ;

    /**
     * 返回有序集 key 中， score 值介于 max 和 min 之间(默认包括等于 max 或 min )的所有的成员。
     * 有序集成员按 score 值递减(从大到小)的次序排列。
     * @param key key
     * @param max score最大值
     * @param min score最小值
     * @return 指定区间内，带有 score 值(可选)的有序集成员的列表
     */
    public Set<String> revrangeByScoreWithSortedSet(final String key, final double max, final double min);

    /**
     * 获取对象
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    public <T> T getObject(final String key, Class<T> type) ;

    /**
     * 获取对象
     * @param key
     * @param object
     * @return
     */
    public List<Object>  getObjectList(final List<String> key, Object object);

    /***
     * 存储对象
     * @param pair
     * @param second
     * @return
     */
    public String setObject(final Pair<String, Object> pair, final int second) ;

    /**
     * 存储对象
     * @param key key
     * @param object  对象
     * @param second
     * @return
     */
    public String setObject(String key, Object object, final int second);
    /**
     * 批量增加对象
     * @param pairList
     * @param second
     * @return
     */
    public String setObjectList(final List<Pair<String, Object>> pairList, final int second);
    /**
     * 存贮对象 永久
     * @param pair
     * @return
     */
    public String setObjectNx(final Pair<String, Object> pair);

    public <K, V> Pair<K, Object> makePair(K key, Object value);

}
