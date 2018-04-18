# SubDomainMapper
## What is it?
SubDomainMapper is a simple library which allow to map a subdomain to a java servlet.  
it is answer to "**how to map a subdomain to a servlet**".
## How to use it?
### 1.Add this project as jar file to your dependencies (or lib) directory
Download: [SubDomainMapper.jar](https://github.com/AlirezaDastyar/SubDomainMpper/blob/master/jar/SubDomainMapper-0.1-SNAPSHOT.jar?raw=true)  
or clone and compile the project. 
### 2.Annotate the your `HttpServlet` class with `@SubDomain()`. 
Example:
```java
@SubDomain(name = "sub")
@WebServlet(name = "MyServlet",urlPatterns = {"/MyServlet"})
public class MyServlet extends HttpServlet{
    ....
}
```
Name parameter in `@SubDomain()` specify the subdomain name that you want to map to this servlet.  
For above example the `http://sub.example.com` will be mapped to the servlet.
### 3.Call `SubDomainFileter.filter()` in a filter class
parameter of `SubDomainFileter.filter(request,response,domainParts)`;  
`request` is the same as request `ServletRequest` in `javax.servlet.Filter#doFilter()`.  
`response` is the same as request `ServletResponse` in `javax.servlet.Filter#doFilter()`.  
`domainParts` indicates parts of splitted (on dot character) of domin name without `www`.  
For example:
```java
@WebFilter(filterName = "MyFilter", urlPatterns = {"/*"})
public class MyFilter extends Filter{
    public doFilter(ServletRequest request, ServletResponse response,FilterChain chain){
          //beforeFiltering 
          // for http://www.example.com  domainParts is 2 [ example, com]
          // for http://www.example.co.uk  domainParts is 3 [example, co, uk]
          boolean sub=SubDomainFilter.filter(request,response,2);
          if(!sub){
              //If no SubDomainMapping happened
          }
          //other filtering code
    }
}
```
and its done.
## Dependencies
### Reflections
Maven repository:
```xml
<dependency>
        <groupId>org.reflections</groupId>
        <artifactId>reflections</artifactId>
        <version>0.9.11</version>
</dependency>
```
## Requierments
This library don't create a subdomain for you.  
If you use hosting control panels go ahead and add a subdomain,  
if don't you have to do it manually.
### Add a subdomin manually
Example for adding `test` as subdomain to `http://www.example.com` in bind9.  
Forward:
```
;
; BIND data file for local loopback interface
;
$TTL	43200
@	IN	SOA	ns1.example.com. root.example.com. (
			      2		; Serial
			  21600		; Refresh
			   1800		; Retry
			 604800		; Expire
			  43200 )	; Negative Cache TTL
;
@	IN	NS	ns1.example.com.
@       IN      NS      ns2.example.com.
@       IN      MX 10   mail
@       IN      A       111.111.111.111
ns1	IN	A	111.111.111.111
ns2     IN      A       111.111.111.111
rev     IN      A       111.111.111.111
mail    IN      A       111.111.111.111
www	IN	A	111.111.111.111
test    IN      A       111.111.111.111
```
Reverse:
```
;
; BIND reverse data file for local loopback interface
;
$TTL	43200
@	IN	SOA	ns1.example.com. root.example.com. (
			      1		; Serial
			  21600		; Refresh
			   1800		; Retry
			 604800		; Expire
			  43200 )	; Negative Cache TTL
;
@	IN  	NS	ns1.example.com.
@       IN      NS      ns2.example.com.
111 	IN  	PTR	rev.example.com.
111     IN      PTR     mail.example.com.
111     IN      PTR     www.example.com.
111     IN      PTR     test.example.com.
```