package data.tinder.profile

import data.ObjectMapper
import domain.profile.DomainGetProfileAnswer
import domain.profile.DomainProfileEmailSettings
import domain.profile.DomainProfileGender
import domain.profile.DomainProfileJob
import domain.profile.DomainProfileJobTitle
import domain.profile.DomainProfileSchool

internal class GetProfileResponseObjectMapper(
    private val profileEmailSettingsMapper: ObjectMapper<ProfileEmailSettings, DomainProfileEmailSettings>,
    private val profileJobMapper: ObjectMapper<ProfileJob, DomainProfileJob>,
    private val profileSchoolObjectMapper: ObjectMapper<ProfileSchool, DomainProfileSchool>)
  : ObjectMapper<GetProfileResponse, DomainGetProfileAnswer> {
  override fun from(source: GetProfileResponse) = with(source) {
    DomainGetProfileAnswer(
        id = id,
        ageFilterMax = ageFilterMax,
        ageFilterMin = ageFilterMin,
        bio = bio,
        birthDate = birthDate,
        blend = blend,
        discoverable = discoverable,
        discoverableParty = discoverableParty,
        distanceFilter = distanceFilter,
        email = email,
        emailSettings = profileEmailSettingsMapper.from(emailSettings),
        facebookId = facebookId,
        gender = DomainProfileGender.fromGenderInt(gender),
        genderFilter = DomainProfileGender.fromGenderInt(genderFilter),
        hideAds = hideAds,
        hideAge = hideAge,
        hideDistance = hideDistance,
        spotifyConnected = spotifyConnected,
        interestedIn = interestedIn.map { DomainProfileGender.fromGenderInt(it) },
        jobs = jobs.map { profileJobMapper.from(it) },
        name = name,
        photos = photos,
        photoOptimizerEnabled = photoOptimizerEnabled,
        photoOptimizerHasResult = photoOptimizerHasResult,
        pingTime = pingTime,
        position = position,
        positionInfo = positionInfo,
        schools = schools.map { profileSchoolObjectMapper.from(it) },
        canCreateSquad = canCreateSquad)
  }
}

internal class ProfileEmailSettingsMapper :
    ObjectMapper<ProfileEmailSettings, DomainProfileEmailSettings> {
  override fun from(source: ProfileEmailSettings) = with (source) {
    DomainProfileEmailSettings(newMatches, messages, promotions)
  }
}

internal class ProfileJobTitleMapper :
    ObjectMapper<ProfileJobTitle?, DomainProfileJobTitle?> {
  override fun from(source: ProfileJobTitle?) = when (source) {
    null -> null
    else -> DomainProfileJobTitle(source.displayed, source.name)
  }
}

internal class ProfileJobMapper(
    private val profileJobTitleMapper: ObjectMapper<ProfileJobTitle?, DomainProfileJobTitle?>)
  : ObjectMapper<ProfileJob, DomainProfileJob> {
  override fun from(source: ProfileJob) = with (source) {
    DomainProfileJob(profileJobTitleMapper.from(title))
  }
}

internal class ProfileSchoolObjectMapper : ObjectMapper<ProfileSchool, DomainProfileSchool> {
  override fun from(source: ProfileSchool) = with (source) {
    DomainProfileSchool(displayed, name)
  }
}
