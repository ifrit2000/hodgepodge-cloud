import pika

from spider_ori.parser import Topic

host = '172.28.0.6'
credential = pika.PlainCredentials('app', 'app')
routing_key = "test.message"
exchange = "testpika"
queue = 'testpika.qu'


def producer(message):
    with pika.BlockingConnection(pika.ConnectionParameters(host, credentials=credential)) as con:
        channel = con.channel()
        channel.exchange_declare(exchange=exchange, exchange_type='topic')
        while True:
            channel.basic_publish(exchange=exchange, routing_key=routing_key, body=message)


def callback(ch, method, properties, body):
    print(" [消费者] %r:%r" % (method.routing_key, body))


def consumer():
    with pika.BlockingConnection(pika.ConnectionParameters(host, credentials=credential)) as con:
        channel = con.channel()
        channel.queue_declare(queue=queue)
        channel.queue_bind(exchange=exchange, queue=queue, routing_key=routing_key)

        channel.basic_consume(on_message_callback=callback, queue=queue, auto_ack=True)
        # channel.basic_consume(callback=callback, queue=queue, no_ack=False)
        channel.start_consuming()


if __name__ == '__main__':
    # producer("23423423")
    # topic = Topic()
    # topic.area = "123"
    # topic.images = ['234', '123412']
    # topic.title = 111
    # a = json.dumps(topic, default=lambda obj: obj.to_dict())
    # print(a)

    topic = Topic.from_json(
        '{"title": 111, "url": "1", "area": "123", "images": ["234", "123412"], "torrent_links": []}')
    print(topic.to_dict())
