<%@page import="com.inmobi.phoenix.kernel.Cache,
                com.inmobi.phoenix.entity.metricmanager.MetricEvent,
                com.inmobi.phoenix.terminationcodes.TerminationCode,
                com.inmobi.phoenix.terminationcodes.TerminationUtils,
                com.inmobi.phoenix.util.metricmanager.MetricManagerUtility,
                com.inmobi.adserve.ccid.CcidResolver,
                com.inmobi.adserve.ccid.BlackberryCcidResolver,
                com.inmobi.adserve.ccid.IpBasedCcidResolver,
                com.inmobi.adserve.ccid.CompositeCcidDetectionService,
                com.inmobi.phoenix.repository.ccid.CcidRepositoryInterface,
                com.inmobi.phoenix.repository.blackberry.BlackberryVendorIDRepositoryInterface,
                com.inmobi.phoenix.entity.Carrier,
                com.inmobi.adserve.ccid.CcidRequest,
                com.inmobi.adserve.ccid.CcidResponse,
                java.util.*" %>
<%@ page language="java"
        contentType="application/json; charset=ISO-8859-1"
        pageEncoding="ISO-8859-1"%>
<%
        //This is an optional request param.
        //If you are trying to find carrier details for a blackberry vendor
        // you should provide the userAgent as a request param.
        String userAgent = request.getParameter("userAgent");

        //This is a compulsory request param for ip based ccid resolution.
        String ipStr = request.getParameter("ipStr");
        
        //This is an optional parameter.
        //Specify it only if you want to query the older feed file.
        String ipFileVersionStr = request.getParameter("feedFileVersion");
        Long ipFileVersion = null;
        try {
          if (ipFileVersionStr != null) {
              ipFileVersion = Long.parseLong(ipFileVersionStr);
          }
        } catch (NumberFormatException e) {
          out.println("NumberFormatException encountered in parsing feedFileVersion");
        }

        Byte fileVersion = ipFileVersion == null ? null : ipFileVersion.byteValue();
        try {
                Cache c = Cache.getInstance("CacheInstance");
                //1. CarrierRepo
                CcidRepositoryInterface carrierRepo = (CcidRepositoryInterface) 
                                       c.getRepositoryLocatory().
                                       getRepositoryInstance("CcidRepository");

                //2. Blackberry Repo
                BlackberryVendorIDRepositoryInterface blackberryRepo = 
                            (BlackberryVendorIDRepositoryInterface)  c.getRepositoryLocatory().
                                       getRepositoryInstance("BlackberryVendorIDRepository");
                //metricEventList
                List<MetricEvent> listMetricEvent = MetricManagerUtility.
                            createNewOrReturnEventListIfNotPresent(null, null);
                
                //terminationCodeList
                @SuppressWarnings("unchecked")
                List<TerminationCode> terminationReasons = TerminationUtils.createNewIfNull(null);
                
                List<CcidResolver> resolvers = new ArrayList<CcidResolver>(2);
                resolvers.add(new BlackberryCcidResolver(blackberryRepo));
                resolvers.add(new IpBasedCcidResolver(carrierRepo));
                CompositeCcidDetectionService ccidDetectionService = 
                            new CompositeCcidDetectionService(Collections.unmodifiableList(resolvers));

                CcidRequest ccidRequest = CcidRequest.builder()
                                .userAgent(userAgent)
                                .fileVersion(fileVersion)
                                .ipv4(ipStr)
                                .requestIpv4(null)
                                .traceMarker(null)
                                .metricEventList(listMetricEvent)
                                .terminationCodeList(terminationReasons)
                                .build();

                CcidResponse ccidResponse = ccidDetectionService.getCarrier(ccidRequest);
                if (ccidResponse.isSuccess()) {
                      Carrier carrier = ccidResponse.getCarrier();
                      if (carrier != null) {
                        out.println(carrier.toString());
                      } else {
                        out.println("Carrier Not Found");
                      }
                
                } else {
                        out.println("Carrier Not Found");
                }

        } catch (Exception e) {
                out.println(e.getMessage());
        }
%>

