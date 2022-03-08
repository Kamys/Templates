package com.kamys.emails

import com.netflix.discovery.EurekaClient
import org.apache.http.client.utils.URIBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.client.DefaultServiceInstance
import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest
import org.springframework.cloud.client.loadbalancer.Request
import org.springframework.stereotype.Component
import java.net.URI

@Component
class MyLoadBalancerClient(
    @Autowired
    private val discoveryClient: EurekaClient
) : LoadBalancerClient {

    override fun choose(serviceId: String): ServiceInstance {
        val application = discoveryClient.getApplication(serviceId) ?: throw Error("Service $serviceId not found")
        val instanceInfo = application.instances.random()
        val info = DefaultServiceInstance(
            instanceInfo.instanceId,
            serviceId,
            instanceInfo.hostName,
            instanceInfo.port,
            false,
        )
        println(info)
        return info
    }

    override fun <T : Any?> choose(serviceId: String, request: Request<T>): ServiceInstance {
        return choose(serviceId)
    }

    override fun <T : Any?> execute(serviceId: String?, request: LoadBalancerRequest<T>?): T {
        TODO("Not yet implemented")
    }

    override fun <T : Any?> execute(
        serviceId: String?,
        serviceInstance: ServiceInstance?,
        request: LoadBalancerRequest<T>?
    ): T {
        TODO("Not yet implemented")
    }

    override fun reconstructURI(instance: ServiceInstance, original: URI): URI {
        val builder = URIBuilder()
        builder.scheme = original.scheme
        builder.host = instance.host
        builder.port = instance.port
        builder.path = original.path
        println(builder.build().toString())
        return builder.build()
    }
}