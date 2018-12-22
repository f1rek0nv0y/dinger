package data.network

import data.ObjectMapper
import io.reactivex.Single

internal abstract class RequestFacade<in RequestModel, MappedRequestModel, ResponseModel, MappedModel>(
    internal val source: RequestSource<MappedRequestModel, ResponseModel>,
    internal val requestMapper: ObjectMapper<RequestModel, MappedRequestModel>,
    private val responseMapper: ObjectMapper<ResponseModel, MappedModel>)
  : Fetchable<RequestModel, MappedModel> {
  override fun fetch(parameters: RequestModel) =
      map(source.fetch(requestMapper.from(parameters)))

  /**
   * Maps items on the given series to another model.
   * @param sourceStream The series whose items are to be mapped.
   */
  fun map(sourceStream: Single<ResponseModel>): Single<MappedModel> =
      sourceStream.map { responseMapper.from(it) }
}
