export default class Measure {
  constructor(updatedDate, measureType, maxCapacity, vaccinationStatus, maskStatus, details) {
    this.updatedDate = updatedDate
    this.measureType = measureType
    this.maxCapacity = maxCapacity
    this.vaccinationStatus = vaccinationStatus
    this.maskStatus = maskStatus
    this.details = details
  }
}
