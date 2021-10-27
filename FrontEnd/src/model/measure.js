export default class Measure {
  constructor(creationDateTime, businessType, maxCapacity, vaccinationStatus, maskStatus, details) {
    this.creationDateTime = creationDateTime
    this.businessType = businessType
    this.maxCapacity = maxCapacity
    this.vaccinationStatus = vaccinationStatus
    this.maskStatus = maskStatus
    this.details = details
  }
}
