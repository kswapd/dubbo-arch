package apps;

/**
 * Created by kongxiangwen on 7/10/18 w:28.
 */
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by kongxiangwen on 7/10/18 w:28.
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.ClientRequestAdapter;
import com.github.kristofa.brave.ClientRequestInterceptor;
import com.github.kristofa.brave.ClientResponseAdapter;
import com.github.kristofa.brave.ClientResponseInterceptor;
import com.github.kristofa.brave.EmptySpanCollectorMetricsHandler;
import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.ServerRequestAdapter;
import com.github.kristofa.brave.ServerRequestInterceptor;
import com.github.kristofa.brave.ServerResponseAdapter;
import com.github.kristofa.brave.ServerResponseInterceptor;
import com.github.kristofa.brave.SpanId;
import com.github.kristofa.brave.TraceData;
import com.github.kristofa.brave.http.HttpSpanCollector;
import com.twitter.zipkin.gen.Endpoint;
public class App {
	private static HttpSpanCollector collector = null;
	private static Brave brave = null;
	private static Brave brave2 = null;

	private static void braveInit(){
		collector = HttpSpanCollector.create("http://10.88.2.112:9411/", new EmptySpanCollectorMetricsHandler());

		brave = new Brave.Builder("appserver").spanCollector(collector).build();
		brave2 = new Brave.Builder("datacenter").spanCollector(collector).build();
	}

	static class Task {
		String name;
		SpanId spanId;
		public Task(String name, SpanId spanId) {
			super();
			this.name = name;
			this.spanId = spanId;
		}
	}

	public static void main(String[] args) throws Exception {
		braveInit();

		final BlockingQueue<Task> queue = new ArrayBlockingQueue<Task>(10);
		Thread thread = new Thread(){
			public void run() {
				while (true) {
					try {
						Task task = queue.take();
						dcHandle(task.name, task.spanId);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();

		{
			ServerRequestInterceptor serverRequestInterceptor = brave.serverRequestInterceptor();
			ServerResponseInterceptor serverResponseInterceptor = brave.serverResponseInterceptor();
			ClientRequestInterceptor clientRequestInterceptor = brave.clientRequestInterceptor();
			ClientResponseInterceptor clientResponseInterceptor = brave.clientResponseInterceptor();





			//serverRequestInterceptor.handle(new ServerRequestAdapterImpl("group_data"));

			/*ClientRequestAdapterImpl clientRequestAdapterImpl = new ClientRequestAdapterImpl("all");
			clientRequestInterceptor.handle(clientRequestAdapterImpl);*/
			//queue.offer(new Task("get_radio_list", clientRequestAdapterImpl.getSpanId()));





			serverRequestInterceptor.handle(new ServerRequestAdapterImpl("group_data"));

			ClientRequestAdapterImpl clientRequestAdapterImpl = new ClientRequestAdapterImpl("get_user_list");
			clientRequestInterceptor.handle(clientRequestAdapterImpl);
			queue.offer(new Task("get_user_list", clientRequestAdapterImpl.getSpanId()));
			Thread.sleep(50);
			clientResponseInterceptor.handle(new ClientResponseAdapterImpl());


			clientRequestAdapterImpl = new ClientRequestAdapterImpl("get_program_list");
			clientRequestInterceptor.handle(clientRequestAdapterImpl);
			queue.offer(new Task("get_program_list", clientRequestAdapterImpl.getSpanId()));
			Thread.sleep(50);
			clientResponseInterceptor.handle(new ClientResponseAdapterImpl());


			serverResponseInterceptor.handle(new ServerResponseAdapterImpl());
			//serverResponseInterceptor.handle(new ServerResponseAdapterImpl());



		}
		Thread.sleep(3000);
	}

	public static void dcHandle(String spanName, SpanId spanId){
		ServerRequestInterceptor serverRequestInterceptor = brave2.serverRequestInterceptor();
		ServerResponseInterceptor serverResponseInterceptor = brave2.serverResponseInterceptor();


		serverRequestInterceptor.handle(new ServerRequestAdapterImpl(spanName, spanId));

		try {
			Thread.sleep(40);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		serverResponseInterceptor.handle(new ServerResponseAdapterImpl());
	}


	static class ServerRequestAdapterImpl implements ServerRequestAdapter {

		Random randomGenerator = new Random();
		SpanId spanId;
		String spanName;

		ServerRequestAdapterImpl(String spanName){
			this.spanName = spanName;
			long startId = randomGenerator.nextLong();
			SpanId spanId = SpanId.builder().spanId(startId).traceId(startId).parentId(startId).build();
			this.spanId = spanId;
		}

		ServerRequestAdapterImpl(String spanName, SpanId spanId){
			this.spanName = spanName;
			this.spanId = spanId;
		}


		public TraceData getTraceData() {
			if (this.spanId != null) {
				return TraceData.builder().spanId(this.spanId).build();
			}
			long startId = randomGenerator.nextLong();
			SpanId spanId = SpanId.builder().spanId(startId).traceId(startId).parentId(startId).build();
			return TraceData.builder().spanId(spanId).build();
		}


		public String getSpanName() {
			return spanName;
		}


		public Collection<KeyValueAnnotation> requestAnnotations() {
			Collection<KeyValueAnnotation> collection = new ArrayList<KeyValueAnnotation>();
			KeyValueAnnotation kv = KeyValueAnnotation.create("radioid", "165646485468486364");
			collection.add(kv);
			return collection;
		}

	}

	static class ServerResponseAdapterImpl implements ServerResponseAdapter {


		public Collection<KeyValueAnnotation> responseAnnotations() {
			Collection<KeyValueAnnotation> collection = new ArrayList<KeyValueAnnotation>();
			KeyValueAnnotation kv = KeyValueAnnotation.create("radioid", "165646485468486364");
			collection.add(kv);
			return collection;
		}

	}

	static class ClientRequestAdapterImpl implements ClientRequestAdapter {

		String spanName;
		SpanId spanId;

		ClientRequestAdapterImpl(String spanName){
			this.spanName = spanName;
		}

		public SpanId getSpanId() {
			return spanId;
		}


		public String getSpanName() {
			return this.spanName;
		}


		public void addSpanIdToRequest(SpanId spanId) {
			//记录传输到远程服务
			System.out.println(spanId);
			if (spanId != null) {
				this.spanId = spanId;
				System.out.println(String.format("trace_id=%s, parent_id=%s, span_id=%s", spanId.traceId, spanId.parentId, spanId.spanId));
			}
		}


		public Collection<KeyValueAnnotation> requestAnnotations() {
			Collection<KeyValueAnnotation> collection = new ArrayList<KeyValueAnnotation>();
			KeyValueAnnotation kv = KeyValueAnnotation.create("radioid", "165646485468486364");
			collection.add(kv);
			return collection;
		}


		public Endpoint serverAddress() {
			return null;
		}

	}

	static class ClientResponseAdapterImpl implements ClientResponseAdapter {


		public Collection<KeyValueAnnotation> responseAnnotations() {
			Collection<KeyValueAnnotation> collection = new ArrayList<KeyValueAnnotation>();
			KeyValueAnnotation kv = KeyValueAnnotation.create("radioname", "火星人1");
			collection.add(kv);
			return collection;
		}

	}
}

